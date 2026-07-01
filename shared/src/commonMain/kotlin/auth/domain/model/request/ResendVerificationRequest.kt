package auth.domain.model.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResendVerificationRequest(
    @SerialName("email")
    val email: String?
)