package user.data.remote.mapper

import user.data.local.entity.UserEntity
import user.data.remote.dto.UserDto

fun UserDto.toEntityModel(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        avatarUrl = avatarUrl,
        emailVerified = emailVerified,
        isAnonymous = isAnonymous,
        upgradedAt = upgradedAt,
    )
}