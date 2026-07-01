package storage.data.remote.mapper

import storage.data.remote.dto.UploadUrlsDto
import storage.domain.model.UploadSession
import storage.domain.model.UploadTarget

fun UploadUrlsDto.toUploadSession(): UploadSession =
    UploadSession(
        sessionId = sessionId,
        uploads = uploads.map { UploadTarget(objectKey = it.objectKey, uploadUrl = it.uploadUrl) }
    )
