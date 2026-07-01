package storage.domain.model

data class UploadSession(
    val sessionId: String,
    val uploads: List<UploadTarget>
)

data class UploadTarget(
    val objectKey: String,
    val uploadUrl: String
)
