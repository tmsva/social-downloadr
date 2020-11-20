package free.thirdpack.instadownloader.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import free.thirdpack.instadownloader.data.AppDatabase
import free.thirdpack.instadownloader.data.DownloadMediaDao
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context, AppDatabase::class.java,
            "social-downloadr-db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideDownloadMediaDao(
        appDatabase: AppDatabase
    ): DownloadMediaDao = appDatabase.downloadMediaDao()
}