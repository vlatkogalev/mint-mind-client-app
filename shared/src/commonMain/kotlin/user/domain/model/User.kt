package user.domain.model

data class User(
    val id: String,

    val email: String,

    val firstName: String,

    val lastName: String,

    val avatarUrl: String?,

    val emailVerified: Boolean,

    val isAnonymous: Boolean,
)