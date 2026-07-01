package feed.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey
    val id: String,

    val title: String,

    val url: String,

    val description: String,

    val imageUrl: String,

    val publishedAt: Long
)