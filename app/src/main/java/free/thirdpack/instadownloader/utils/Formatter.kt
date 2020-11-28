package free.thirdpack.instadownloader.utils

import android.app.DownloadManager.*
import android.content.Context
import free.thirdpack.instadownloader.R
import java.text.DateFormat
import java.util.*

const val RESERVED_SYSTEM_CHARS = "[\\\\\\\\/:*?\\\"<>|]"

object Formatter {

    @JvmStatic
    fun getDownloadStatus(context: Context, statusCode: Int): String {
        if (statusCode == STATUS_RUNNING)
            return context.getString(R.string.downloading)

        val status = when (statusCode) {
            STATUS_FAILED -> R.string.failed
            STATUS_PAUSED -> R.string.paused
            STATUS_PENDING -> R.string.pending
            STATUS_SUCCESSFUL -> R.string.complete
            else -> R.string.empty
        }

        return with(context) {
            getString(R.string.download_status, getString(status))
        }
    }

    @JvmStatic
    fun formatProgressDiff(
            context: Context,
            bytesSoFar: Long,
            totalSizeBytes: Long
    ): String {
        val formatSoFar = formatFileSize(context, bytesSoFar)
        val formatTotal = formatFileSize(context, totalSizeBytes)
        return "$formatSoFar / $formatTotal"
    }

    @JvmStatic
    fun formatFilename(filename: String?): String =
            if (filename.isNullOrEmpty())
                "Social_Media${DateFormat.getDateInstance().format(Date())}"
            else
                filename.replace(RESERVED_SYSTEM_CHARS.toRegex(), "_")

    @JvmStatic
    fun formatFileSize(context: Context, sizeBytes: Long): String =
            android.text.format.Formatter.formatShortFileSize(context, sizeBytes)
}