package notification.domain

import app.domain.NetworkError
import app.domain.model.NetworkResult

interface NotificationTopicsRepository {
    suspend fun getAssignedTopics(): NetworkResult<List<String>, NetworkError>
}