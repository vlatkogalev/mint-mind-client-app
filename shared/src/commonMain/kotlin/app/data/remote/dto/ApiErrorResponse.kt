package app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
    @SerialName("code")
    val code: String? = null,

    @SerialName("message")
    val message: String? = null
)
