package free.thirdpack.instadownloader.data

import com.google.gson.annotations.SerializedName

data class Node(
    @field:SerializedName("__typename")
    val type: String,
    @field:SerializedName("is_video")
    val isVideo: Boolean = false,
    @field:SerializedName("title")
    val title: String,
    @field:SerializedName("display_url")
    val displayUrl: String,
    @field:SerializedName("display_resources")
    val displayResources: List<DisplayRes>,
    @field:SerializedName("video_url")
    val videoUrl: String?,
    @field:SerializedName("edge_sidecar_to_children")
    val sidecar: Sidecar?
) {
    companion object {
        const val IMAGE = "GraphImage"
        const val VIDEO = "GraphVideo"
        const val SIDECAR = "GraphSidecar"
    }
}
