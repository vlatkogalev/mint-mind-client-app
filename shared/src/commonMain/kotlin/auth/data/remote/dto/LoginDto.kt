package auth.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
    @SerialName("accessToken")
    val accessToken: String,

    @SerialName("refreshToken")
    val refreshToken: String,

    @SerialName("accessTokenExpiresInSeconds")
    val accessTokenExpiresInSeconds: Int,

    @SerialName("refreshTokenExpiresInSeconds")
    val refreshTokenExpiresInSeconds: Int
)
