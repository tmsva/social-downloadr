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

class MainViewModel(val app: Application) : AndroidViewModel(app) {
    val retrofit = Retrofit.Builder()
        .baseUrl(MainActivity.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(InstagramService::class.java)

    fun getMetaPost(url: String) = viewModelScope.launch {
        val metaPost = service.getMetaPost(url)
        Log.d(
            "TAK", "gettinmetapost:> ${metaPost.graphql}"
        )
        Log.d(
            "TAK", "simplevideourl:> ${metaPost.graphql.shortcodeMedia.simpleVideoUrl()}"
        )
        val media = metaPost.graphql.shortcodeMedia
        val request = DownloadManager.Request(Uri.parse(media.videoUrl)).apply {
            setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setTitle(media.title)
            setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "${MainActivity.DOWNLOAD_FOLDER}${media.title}"
            )
        }
        val manager = app.getSystemService(Context.DOWNLOAD_SERVICE)
                as DownloadManager
        manager.enqueue(request)
    }
}