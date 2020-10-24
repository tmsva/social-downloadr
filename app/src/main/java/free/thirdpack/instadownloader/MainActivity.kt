package free.thirdpack.instadownloader

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import free.thirdpack.instadownloader.data.Node
import free.thirdpack.instadownloader.data.Result
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
        intent?.let { handleMediaIntent(it) }
    }

    private fun handleMediaIntent(intent: Intent) = with(intent) {
        if (type == TYPE_TEXT_PLAIN && clipData != null) {
            val url = clipData!!.getItemAt(0).text as String
            Log.d("TAK", url)
            if (url.contains(DOMAIN)) {
                val postId = url.split("/")[4]
                Log.d("TAK", "getting meta data")
                viewModel.getMetaPost(postId).observe(this@MainActivity) { mediaCount ->
                    when (mediaCount) {
                        //is Result.Success ->
                        is Result.Loading -> showMediaSelectionDialog(mediaCount.data!!)
                        is Result.Error ->
                            Toast.makeText(
                                this@MainActivity,
                                "Error al realizar la descarga",
                                Toast.LENGTH_SHORT
                            ).show()
                    }
                }
            }
        }
    }


    private fun showMediaSelectionDialog(nodes: List<Node>) {
        val totalSize = nodes.size + 1
        val choiceItems = Array(totalSize) { if (it == 0) "All" else "$it" }
        val checkedItems = BooleanArray(totalSize) { it == 0 }

        AlertDialog.Builder(this)
            .setTitle("Select item(s) to download")
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(android.R.string.ok) { dialog, which ->
                viewModel.batchMediaDownload(checkedItems, nodes)
            }
            .setMultiChoiceItems(choiceItems, checkedItems) { dialog, which, isChecked ->
                if (isChecked) {
                    val listView = (dialog as AlertDialog).listView
                    if (which == 0) {
                        checkedItems.fill(false, 1)
                        for (i in 1 until totalSize)
                            if (listView.isItemChecked(i))
                                listView.setItemChecked(i, false)
                    } else if (listView.isItemChecked(0)) {
                        checkedItems[0] = false
                        listView.setItemChecked(0, false)
                    }
                }
            }
            .show()
    }
}