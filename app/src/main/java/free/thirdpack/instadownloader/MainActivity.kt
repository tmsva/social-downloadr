package free.thirdpack.instadownloader

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import free.thirdpack.instadownloader.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    companion object {
        const val TYPE_TEXT_PLAIN = "text/plain"
        const val DOMAIN = "instagram.com"
        const val DOWNLOAD_FOLDER = "InstaDownloader"
        const val BASE_URL = "https://$DOMAIN/p/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.run {
            if (type == TYPE_TEXT_PLAIN && clipData != null) {
                val url = clipData!!.getItemAt(0).text as String
                if (url.contains(DOMAIN)) {
                    val postId = url.split("/")[4]
                    Log.d("TAK", "getting meta data")
                    viewModel.getMetaPost(postId)
                }
            }
        }
    }
}