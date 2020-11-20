package free.thirdpack.instadownloader.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "download_media")
data class DownloadMedia(
    @PrimaryKey val uid: Long,
    val status: String,
    var progress: Int = -1,
    var uri: String?,
    val size: Int
)