package feed.domain.model

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

data class Post(
    val id: String,
    val title: String,
    val url: String,
    val description: String,
    val imageUrl: String,
    val publishedAt: Long
) {
    companion object {
        val dummyItem = Post(
            id = "1",
            title = LoremIpsum().values.first().take(90),
            url = "https://example.com/post",
            description = "Description",
            imageUrl = "https://example.com/image.jpg",
            publishedAt = 1677580800000L
        )

        fun dummyItemsList(count: Int = 5): List<Post> {
            return (1..count).map {
                dummyItem.copy(id = it.toString())
            }
        }
    }
}