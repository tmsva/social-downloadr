package free.thirdpack.instadownloader.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "download_media")
data class DownloadMedia(
    @PrimaryKey val uid: Long,
    val title: String?,
    var status: Int,
    var progress: Int = -1,
    var uri: String?,
    var bytesDownloaded: Int = 0,
    var totalSizeBytes: Int,
    var isComplete: Boolean = false
)