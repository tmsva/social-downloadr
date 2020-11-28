package free.thirdpack.instadownloader.utils

import android.app.DownloadManager
import android.app.DownloadManager.COLUMN_ID
import android.app.DownloadManager.COLUMN_LOCAL_URI
import android.database.Cursor
import android.net.Uri
import free.thirdpack.instadownloader.data.DownloadMedia

object Mapper {
    @JvmStatic
    fun Cursor.mapDownloadMedia(): DownloadMedia {
        val status = getInt(DownloadManager.COLUMN_STATUS)
        val progress = if (status == DownloadManager.STATUS_SUCCESSFUL) 100 else getProgress()
        val uri = getString(COLUMN_LOCAL_URI)
        val filename = Uri.parse(uri?:"").lastPathSegment

        return DownloadMedia(
                getLong(COLUMN_ID),
                filename,
                status,
                progress,
                uri,
                getInt(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR),
                getInt(DownloadManager.COLUMN_TOTAL_SIZE_BYTES),
                progress == 100
        )
    }
}

private fun Cursor.getProgress(): Int {
    val downloaded = getInt(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
    val total = getInt(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
    val progress = (downloaded * 1f / total) * 100
    return progress.toInt()
}