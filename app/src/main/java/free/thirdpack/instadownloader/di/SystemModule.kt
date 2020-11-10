package free.thirdpack.instadownloader.di

import android.app.Application
import android.app.DownloadManager
import android.content.ClipboardManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object SystemModule {

    @Provides
    fun provideDownloadManager(
        app: Application
    ): DownloadManager =
        app.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    @Provides
    fun provideClipboardManager(
        app: Application
    ): ClipboardManager =
        app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

}