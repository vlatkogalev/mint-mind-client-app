package user.domain

import app.domain.NetworkError
import app.domain.model.EmptyNetworkResult
import kotlinx.coroutines.flow.Flow
import user.domain.model.User

interface UserRepository {
    suspend fun storeUser(): EmptyNetworkResult<NetworkError>
    fun getUser(): Flow<User?>
    suspend fun getUserId(): String?
    suspend fun clearLocalUserSession()
}
