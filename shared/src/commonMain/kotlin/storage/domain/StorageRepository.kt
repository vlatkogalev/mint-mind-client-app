package storage.domain

import app.domain.NetworkError
import app.domain.model.NetworkResult
import storage.domain.model.UploadSession

interface StorageRepository {

    suspend fun getUploadUrls(fileCount: Int): NetworkResult<UploadSession, NetworkError>

    suspend fun uploadFile(
        uploadUrl: String,
        fileData: ByteArray,
        contentType: String = "image/jpeg"
    ): NetworkResult<Unit, NetworkError>
}
