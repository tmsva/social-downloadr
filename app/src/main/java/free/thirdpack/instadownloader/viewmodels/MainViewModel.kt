package free.thirdpack.instadownloader.viewmodels

import android.app.Application
import android.app.DownloadManager
import android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import free.thirdpack.instadownloader.MainActivity
import free.thirdpack.instadownloader.api.InstagramService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainViewModel(app: Application) : AndroidViewModel(app) {
    val downloadManager = app.getSystemService(Context.DOWNLOAD_SERVICE)
            as DownloadManager
    val retrofit = Retrofit.Builder()
        .baseUrl(MainActivity.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(InstagramService::class.java)

    fun getMetaPost(url: String) = viewModelScope.launch {
        val metaPost = service.getMetaPost(url)
        val media = metaPost.graphql.shortcodeMedia
        Log.d("TAK", media.toString())
        val downloadUrl = if (media.isVideo) media.videoUrl else media.displayResources.last().src
        Log.d("TAK", "$downloadUrl")
        val request = DownloadManager.Request(Uri.parse(downloadUrl)).apply {
            setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "${MainActivity.DOWNLOAD_FOLDER}${Date()}"
            )
        }
        downloadManager.enqueue(request)
    }
}