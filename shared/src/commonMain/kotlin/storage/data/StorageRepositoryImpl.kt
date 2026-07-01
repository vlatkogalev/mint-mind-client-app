package storage.data

import app.data.remote.constructUrl
import app.data.remote.safeCall
import app.domain.NetworkError
import app.domain.model.NetworkResult
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import storage.data.remote.dto.UploadUrlsDto
import storage.data.remote.dto.UploadUrlsRequest
import storage.data.remote.mapper.toUploadSession
import storage.domain.StorageRepository
import storage.domain.model.UploadSession

class StorageRepositoryImpl(
    private val httpClient: HttpClient,
    private val s3HttpClient: HttpClient,
) : StorageRepository {

    override suspend fun getUploadUrls(
        fileCount: Int
    ): NetworkResult<UploadSession, NetworkError> {
        val result = safeCall<UploadUrlsDto> {
            httpClient.post(urlString = constructUrl("/storage/upload-urls")) {
                setBody(UploadUrlsRequest(fileCount = fileCount))
            }
        }

        return when (result) {
            is NetworkResult.Success -> NetworkResult.Success(result.data.toUploadSession())

            is NetworkResult.Error -> NetworkResult.Error(result.error)
        }
    }

    override suspend fun uploadFile(
        uploadUrl: String,
        fileData: ByteArray,
        contentType: String
    ): NetworkResult<Unit, NetworkError> {
        val response = try {
            s3HttpClient.put(urlString = uploadUrl) {
                contentType(ContentType.parse(contentType))
                setBody(fileData)
            }
        } catch (_: UnresolvedAddressException) {
            return NetworkResult.Error(NetworkError.NoInternet)
        } catch (_: Exception) {
            currentCoroutineContext().ensureActive()
            return NetworkResult.Error(NetworkError.Unknown(null))
        }

        return if (response.status.value in 200..299) {
            NetworkResult.Success(Unit)
        } else {
            NetworkResult.Error(NetworkError.ServerError(statusCode = response.status))
        }
    }
}
