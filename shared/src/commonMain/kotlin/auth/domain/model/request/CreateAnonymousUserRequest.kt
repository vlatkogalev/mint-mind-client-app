package auth.domain.model.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateAnonymousUserRequest(
    @SerialName("installationId")
    val installationId: String?
)