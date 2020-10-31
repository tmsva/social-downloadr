package free.thirdpack.instadownloader.viewmodels

import android.app.Application
import android.app.DownloadManager
import android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import free.thirdpack.instadownloader.MainActivity
import free.thirdpack.instadownloader.api.InstagramService
import free.thirdpack.instadownloader.data.Node
import free.thirdpack.instadownloader.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainViewModel @ViewModelInject constructor(
    app: Application,
    private val igService: InstagramService,
    private val downloadManager: DownloadManager,
) : AndroidViewModel(app) {

    fun getMetaPost(url: String) = liveData {
        val metaPost = igService.getMetaPost(url)
        val node = metaPost.graphql.shortcodeMedia
        Log.d("TAK", node.toString())
        if (node.type == Node.SIDECAR) {
            val sideNodes = node.sidecar!!.edges.map { it.node }
            emit(Result.Loading(sideNodes))
        } else {
            downloadMedia(node)
            emit(Result.Success(null))
        }
    }

    suspend fun downloadMedia(node: Node) = withContext(Dispatchers.IO) {
        val downloadUrl =
            if (node.isVideo) node.videoUrl else node.displayResources.last().src
        Log.d("TAK", "$downloadUrl")
        val request = DownloadManager.Request(Uri.parse(downloadUrl)).apply {
            setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "${MainActivity.DOWNLOAD_FOLDER}${Date()}"
            )
        }.setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        downloadManager.enqueue(request)
    }

    fun batchMediaDownload(
        selectedNodes: BooleanArray,
        nodes: List<Node>
    ) = viewModelScope.launch {
        val selectedMedia = if (selectedNodes[0]) nodes else
            nodes.filterIndexed { index, _ -> selectedNodes[index + 1] }
        selectedMedia.forEach { downloadMedia(it) }
    }
}