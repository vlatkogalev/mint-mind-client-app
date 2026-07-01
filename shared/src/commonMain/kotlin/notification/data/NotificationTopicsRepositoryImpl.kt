package notification.data

import app.data.remote.constructUrl
import app.data.remote.safeCall
import app.domain.NetworkError
import app.domain.model.NetworkResult
import app.domain.model.map
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import notification.domain.NotificationTopicsRepository

class NotificationTopicsRepositoryImpl(
    private val httpClient: HttpClient,
) : NotificationTopicsRepository {

    override suspend fun getAssignedTopics(): NetworkResult<List<String>, NetworkError> {
        return safeCall<List<String>> {
            httpClient.get(urlString = constructUrl("/api/user/notification/topics"))
        }.map { topics ->
            topics
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .distinct()
        }
    }
}