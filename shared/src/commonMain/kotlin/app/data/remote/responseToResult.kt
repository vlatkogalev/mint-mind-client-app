package app.data.remote

import app.data.remote.dto.ApiErrorResponse
import app.domain.NetworkError
import app.domain.model.NetworkResult
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> responseToResult(
    response: HttpResponse,
): NetworkResult<T, NetworkError> {
    if (response.status.value in 200..299) {
        return try {
            NetworkResult.Success(response.body<T>())
        } catch (_: NoTransformationFoundException) {
            NetworkResult.Error(NetworkError.Serialization)
        }
    }

    return try {
        val errorResponse = response.body<ApiErrorResponse>()
        NetworkResult.Error(
            NetworkError.ApiError(
                status = response.status,
                code = errorResponse.code,
                message = errorResponse.message
            )
        )
    } catch (_: Exception) {
        when (response.status.value) {
            in 400..499 -> NetworkResult.Error(NetworkError.ClientError(statusCode = response.status))
            in 500..599 -> NetworkResult.Error(NetworkError.ServerError(statusCode = response.status))
            else -> NetworkResult.Error(NetworkError.Unknown(statusCode = response.status))
        }
    }
}
