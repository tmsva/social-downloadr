package free.thirdpack.instadownloader.data

import com.google.gson.annotations.SerializedName

data class Graphql(
    @SerializedName("shortcode_media")
    val shortcodeMedia: IgMedia
)
