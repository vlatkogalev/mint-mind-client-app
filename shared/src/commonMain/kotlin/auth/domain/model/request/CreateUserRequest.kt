package auth.domain.model.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    @SerialName("email")
    val email: String?,

    @SerialName("password")
    val password: String?,

    @SerialName("firstName")
    val firstName: String?,

    @SerialName("lastName")
    val lastName: String?
)