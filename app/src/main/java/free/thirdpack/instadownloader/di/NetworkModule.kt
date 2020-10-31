package free.thirdpack.instadownloader.di

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object NetworkModule {

    @Provides
    fun provideDownloadManager(
        activity: Activity
    ): DownloadManager =
        activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
}