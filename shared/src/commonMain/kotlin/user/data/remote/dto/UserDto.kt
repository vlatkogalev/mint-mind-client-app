package user.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id")
    val id: String,

    @SerialName("email")
    val email: String,

    @SerialName("firstName")
    val firstName: String,

    @SerialName("lastName")
    val lastName: String,

    @SerialName("avatarUrl")
    val avatarUrl: String?,

    @SerialName("emailVerified")
    val emailVerified: Boolean,

    @SerialName("isAnonymous")
    val isAnonymous: Boolean?,

    @SerialName("upgradedAt")
    val upgradedAt: String?
)
