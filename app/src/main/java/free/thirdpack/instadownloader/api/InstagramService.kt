package free.thirdpack.instadownloader.api

import free.thirdpack.instadownloader.data.MetaPost
import retrofit2.http.GET
import retrofit2.http.Path

interface InstagramService {

    @GET("{postId}/?__a=1")
    suspend fun getMetaPost(@Path("postId") postId: String): MetaPost
}