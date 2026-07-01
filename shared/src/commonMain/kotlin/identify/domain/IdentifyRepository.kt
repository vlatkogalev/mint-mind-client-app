package identify.domain

import app.domain.NetworkError
import app.domain.model.EmptyNetworkResult
import identify.data.remote.dto.SaveToCollectionRequest

interface IdentifyRepository {
    suspend fun saveToCollection(request: SaveToCollectionRequest): EmptyNetworkResult<NetworkError>
}