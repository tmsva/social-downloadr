package free.thirdpack.instadownloader.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DownloadMediaDao {
    @Query("SELECT * FROM download_media WHERE uid = :downloadId")
    fun getById(downloadId: Long): DownloadMedia

    @Query("SELECT * FROM download_media")
    fun getAll(): LiveData<List<DownloadMedia>>

    @Insert
    suspend fun insert(vararg medias: DownloadMedia)

    @Update
    suspend fun update(vararg medias: DownloadMedia)
}