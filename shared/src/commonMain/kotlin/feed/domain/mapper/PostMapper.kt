package feed.domain.mapper

import feed.data.local.entity.PostEntity
import feed.domain.model.Post

fun List<PostEntity>.toPosts(): List<Post> = map { it.toPost() }

fun PostEntity.toPost(): Post = Post(
    id = id,
    title = title,
    url = url,
    description = description,
    imageUrl = imageUrl,
    publishedAt = publishedAt
)