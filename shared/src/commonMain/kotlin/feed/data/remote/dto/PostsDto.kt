package feed.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostsDto(
    @SerialName("articles")
    val articles: List<Article>,

    @SerialName("nextCursor")
    val nextCursor: Long?
) {
    @Serializable
    data class Article(
        @SerialName("id")
        val id: String,

        @SerialName("title")
        val title: String,

        @SerialName("link")
        val link: String,

        @SerialName("description")
        val description: String,

        @SerialName("imageUrl")
        val imageUrl: String,

        @SerialName("publishedAt")
        val publishedAt: Long
    )
}
