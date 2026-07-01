package user.domain.mapper

import user.data.local.entity.UserEntity
import user.domain.model.User

fun UserEntity.toDomainModel(): User {
    return User(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        avatarUrl = avatarUrl,
        emailVerified = emailVerified,
        isAnonymous = isAnonymous == true,
    )
}