package free.thirdpack.instadownloader.di

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    fun provideDownloadManager(
        app: Application
    ): DownloadManager =
        app.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
}