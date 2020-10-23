package free.thirdpack.instadownloader.data

import com.google.gson.annotations.SerializedName

data class ShortcodeMedia(
    @field:SerializedName("is_video")
    val isVideo: Boolean = false,
    @field:SerializedName("title")
    val title: String,
    @field:SerializedName("display_url")
    val displayUrl: String,
    @field:SerializedName("display_resources")
    val displayResources: List<DisplayRes>,
    @field:SerializedName("video_url")
    val videoUrl: String?
)
