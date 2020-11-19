package free.thirdpack.instadownloader.data

import android.app.DownloadManager
import android.app.DownloadManager.*
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import free.thirdpack.instadownloader.MainActivity
import free.thirdpack.instadownloader.api.InstagramService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.schedule

@Singleton
class DownloadRepository @Inject constructor(
    private val clipboard: ClipboardManager,
    private val downloadManager: DownloadManager,
    private val igService: InstagramService
) {

    private val mMediaQueue = MutableLiveData<List<IgMedia>?>()
    private val mDownloads = MutableLiveData<MutableMap<Long, Int>>(mutableMapOf())
    private val mClipboardPaste = MutableLiveData<CharSequence>()

    val clipboardPaste = mClipboardPaste as LiveData<CharSequence>
    val downloads = mDownloads as LiveData<MutableMap<Long, Int>>

    val mediaQueue = mMediaQueue.map {
        if (it == null || it.isEmpty())
            -1
        else if (it.size > 1)
            it.size
        else 0
    }

    suspend fun checkUrl(url: String) = withContext(Dispatchers.IO) {
        val postId = url.split("/")[4]
        val metaPost = igService.getMetaPost(postId)
        val node = metaPost.graphql.shortcodeMedia
        Log.d("TAK", node.toString())
        if (node.type == IgMedia.SIDECAR) {
            val sideNodes = node.sidecar!!.edges.map { it.igMedia }
            mMediaQueue.postValue(sideNodes)
        } else {
            mMediaQueue.postValue(listOf(node))
            downloadMedia(node)
        }
    }

    suspend fun downloadMedia(igMedia: IgMedia) = withContext(Dispatchers.IO) {
        val downloadUrl =
            if (igMedia.isVideo) igMedia.videoUrl else igMedia.displayResources.last().src
        Log.d("TAK", "$downloadUrl")
        val request = DownloadManager.Request(Uri.parse(downloadUrl)).apply {
            setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "${MainActivity.DOWNLOAD_FOLDER}${Date()}"
            )
        }.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        enqueueDownload(request)
    }

    private fun enqueueDownload(request: Request) {
        val downloadId = downloadManager.enqueue(request)
        val downloadsMap = mDownloads.value!!
        scheduleProgress(downloadId, downloadsMap)
        Log.d("TAKKstart", downloadsMap.toString())
        mDownloads.postValue(downloadsMap)
    }

    private fun scheduleProgress(downloadId: Long, downloads: MutableMap<Long, Int>) {
        downloads.set(downloadId, -1)
        Timer().schedule(100, 300) {
            val download = downloadManager.query(Query().setFilterById(downloadId))
            download.moveToFirst()
            val status = download.run { getInt(getColumnIndex(COLUMN_STATUS)) }
            val progress = if (status == STATUS_SUCCESSFUL) {
                cancel()
                100
            } else getProgress(download)
            download.close()
            downloads.set(downloadId, progress)
            Log.d("TAKKupdate", downloads.toString())
            mDownloads.postValue(downloads)
        }
    }

    private fun getProgress(download: Cursor): Int = with(download) {
        val downloaded = getInt(getColumnIndex(COLUMN_BYTES_DOWNLOADED_SO_FAR))
        val total = getInt(getColumnIndex(COLUMN_TOTAL_SIZE_BYTES))
        val progress = (downloaded * 1f / total) * 100
        progress.toInt()
    }

    suspend fun batchMediaDownload(
        selectedNodes: BooleanArray
    ) {
        val media = mMediaQueue.value!!
        val selectedMedia = if (selectedNodes[0]) media else
            media.filterIndexed { index, _ -> selectedNodes[index + 1] }
        selectedMedia.forEach { downloadMedia(it) }
    }

    fun clipboardPaste() = with(clipboard) {
        if (hasPrimaryClip() && primaryClipDescription!!.hasMimeType(MIMETYPE_TEXT_PLAIN)) {
            val item = primaryClip!!.getItemAt(0).text
            mClipboardPaste.value = item
        }
    }
}