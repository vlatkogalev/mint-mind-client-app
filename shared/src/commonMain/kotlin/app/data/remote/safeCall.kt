package app.data.remote

import app.domain.NetworkError
import app.domain.model.NetworkResult
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse,
): NetworkResult<T, NetworkError> {
    val response = try {
        execute()
    } catch (_: UnresolvedAddressException) {
        return NetworkResult.Error(NetworkError.NoInternet)
    } catch (_: SerializationException) {
        return NetworkResult.Error(NetworkError.Serialization)
    } catch (_: Exception) {
        currentCoroutineContext().ensureActive()
        return NetworkResult.Error(NetworkError.Unknown(null))
    }

    return responseToResult(response)
}