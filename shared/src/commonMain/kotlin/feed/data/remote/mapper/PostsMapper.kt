package feed.data.remote.mapper

import feed.data.local.entity.PostEntity
import feed.data.remote.dto.PostsDto
import feed.domain.model.Post

fun PostsDto.toPostEntities(): List<PostEntity> {
    return articles.map {
        PostEntity(
            id = it.id,
            title = it.title,
            url = it.link,
            description = it.description,
            imageUrl = it.imageUrl,
            publishedAt = it.publishedAt
        )
    }
}

fun PostsDto.toPosts(): List<Post> {
    return articles.map {
        Post(
            id = it.id,
            title = it.title,
            url = it.link,
            description = it.description,
            imageUrl = it.imageUrl,
            publishedAt = it.publishedAt
        )
    }
}