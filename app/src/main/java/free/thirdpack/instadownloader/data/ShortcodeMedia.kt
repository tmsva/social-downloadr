package free.thirdpack.instadownloader.data

import com.google.gson.annotations.SerializedName

data class ShortcodeMedia(
    @SerializedName("is_video")
    val isVideo: Boolean = false,
    @SerializedName("title")
    val title: String,
    @SerializedName("display_url")
    val displayUrl: String,
    @SerializedName("display_resources")
    val displayResources: List<DisplayRes>,
    @SerializedName("video_url")
    val videoUrl: String?
) {
    companion object {
        const val VIDEO_SUFFIX = ".mp4"
    }
    fun simpleVideoUrl(): String? = videoUrl?.substringBefore(VIDEO_SUFFIX) + VIDEO_SUFFIX
}
