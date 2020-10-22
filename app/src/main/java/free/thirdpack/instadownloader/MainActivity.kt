package free.thirdpack.instadownloader

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    companion object {
        const val TYPE_TEXT_PLAIN = "text/plain"
        const val DOMAIN = "instagram.com"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intent.run {
            if (type == TYPE_TEXT_PLAIN && clipData != null) {
                val url = clipData!!.getItemAt(0).text as String
                if (url.contains(DOMAIN)) {
                    val metaUrl = url.replaceAfter("/?", "__a=1")
                    Log.d("TAK", "meta url:> $metaUrl")
                }
            }
        }
    }
}