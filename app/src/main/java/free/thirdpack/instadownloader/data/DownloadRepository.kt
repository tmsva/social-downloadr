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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.schedule

@Singleton
class DownloadRepository @Inject constructor(
    private val clipboard: ClipboardManager,
    private val downloadManager: DownloadManager,
    private val igService: InstagramService,
    private val downloadMediaDao: DownloadMediaDao
) {

    private val mMediaQueue = MutableLiveData<List<IgMedia>?>()

    private val mClipboardPaste = MutableLiveData<CharSequence>()

    val clipboardPaste = mClipboardPaste as LiveData<CharSequence>

    val downloads = downloadMediaDao.getAll()

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

    private suspend fun enqueueDownload(request: Request) {
        val downloadId = downloadManager.enqueue(request)
        insertDownloadMedia(downloadId)
        scheduleProgressUpdate(downloadId)
    }

    private suspend fun insertDownloadMedia(downloadId: Long) = withContext(Dispatchers.IO) {
        getManagerDownload(downloadId)?.apply {
            val downloadMedia = DownloadMedia(
                downloadId,
                getString(getColumnIndex(COLUMN_STATUS)),
                0,
                getString(getColumnIndex(COLUMN_LOCAL_URI)),
                getInt(getColumnIndex(COLUMN_TOTAL_SIZE_BYTES))
            )
            downloadMediaDao.insert(downloadMedia)
        }!!.close()
    }

    private fun getManagerDownload(downloadId: Long): Cursor? {
        val download = downloadManager.query(Query().setFilterById(downloadId))
        return download.takeIf { it.moveToFirst() }
    }

    private fun scheduleProgressUpdate(downloadId: Long) = Timer().schedule(100, 300) {
        GlobalScope.launch(Dispatchers.IO) {
            getManagerDownload(downloadId)?.apply {
                val download = downloadMediaDao.getById(downloadId)
                val status = getInt(getColumnIndex(COLUMN_STATUS))
                val progress = if (status == STATUS_SUCCESSFUL) {
                    this@schedule.cancel()
                    download.uri = getString(getColumnIndex(COLUMN_LOCAL_URI))
                    100
                } else getProgress(this)
                download.progress = progress
                downloadMediaDao.update(download)
            }!!.close()
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