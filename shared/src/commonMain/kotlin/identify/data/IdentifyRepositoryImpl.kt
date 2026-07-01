package identify.data

import app.data.remote.constructUrl
import app.data.remote.safeCall
import app.domain.NetworkError
import app.domain.model.EmptyNetworkResult
import app.domain.model.asEmptyDataNetworkResult
import identify.data.remote.dto.SaveToCollectionDto
import identify.data.remote.dto.SaveToCollectionRequest
import identify.domain.IdentifyRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class IdentifyRepositoryImpl(
    private val httpClient: HttpClient,
) : IdentifyRepository {

    override suspend fun saveToCollection(
        request: SaveToCollectionRequest
    ): EmptyNetworkResult<NetworkError> {
        return safeCall<SaveToCollectionDto> {
            httpClient.post(urlString = constructUrl("/coins")) {
                setBody(request)
            }
        }.asEmptyDataNetworkResult()
    }
}
