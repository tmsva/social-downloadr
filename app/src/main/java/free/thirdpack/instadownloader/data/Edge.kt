package free.thirdpack.instadownloader.data

import com.google.gson.annotations.SerializedName

data class Edge(
    @field:SerializedName("node")
    val igMedia: IgMedia
)