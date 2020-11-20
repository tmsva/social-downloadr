package free.thirdpack.instadownloader.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DownloadMedia::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun downloadMediaDao(): DownloadMediaDao
}