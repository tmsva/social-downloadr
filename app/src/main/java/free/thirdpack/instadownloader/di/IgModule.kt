package free.thirdpack.instadownloader.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import free.thirdpack.instadownloader.MainActivity
import free.thirdpack.instadownloader.api.InstagramService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ActivityComponent::class)
object IgModule {

    @Provides
    fun provideInstagramService(): InstagramService =
        Retrofit.Builder()
            .baseUrl(MainActivity.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InstagramService::class.java)
}