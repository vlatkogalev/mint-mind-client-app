package app.domain

import app.presentation.util.UiText
import io.ktor.http.HttpStatusCode
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.error_client
import mintmind.shared.generated.resources.error_no_internet
import mintmind.shared.generated.resources.error_serialization
import mintmind.shared.generated.resources.error_server
import mintmind.shared.generated.resources.error_unknown

interface Error

sealed interface NetworkError : Error {
    data object NoInternet : NetworkError
    data object Serialization : NetworkError
    data class ClientError(val statusCode: HttpStatusCode) : NetworkError
    data class ServerError(val statusCode: HttpStatusCode) : NetworkError
    data class Unknown(val statusCode: HttpStatusCode?) : NetworkError
    data class ApiError(
        val status: HttpStatusCode? = null,
        val code: String? = null,
        val message: String? = null
    ) : NetworkError
}

fun NetworkError.toErrorMessage(): UiText {
    return when (this) {
        NetworkError.NoInternet -> UiText.StaticResource(Res.string.error_no_internet)

        NetworkError.Serialization -> UiText.StaticResource(Res.string.error_serialization)

        is NetworkError.ClientError -> UiText.StaticResource(
            Res.string.error_client,
            statusCode.value
        )

        is NetworkError.ServerError -> UiText.StaticResource(
            Res.string.error_server,
            statusCode.value
        )

        is NetworkError.Unknown -> statusCode?.let {
            UiText.DynamicString("Error ${it.value}: ${it.description}")
        } ?: UiText.StaticResource(Res.string.error_unknown)

        is NetworkError.ApiError -> {
            if (message.isNullOrEmpty()) {
                return UiText.StaticResource(Res.string.error_unknown)
            }

            UiText.DynamicString(message)
        }
    }
}