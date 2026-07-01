package user.data

import app.data.local.AppDatabase
import app.data.remote.constructUrl
import app.data.remote.safeCall
import app.domain.NetworkError
import app.domain.model.EmptyNetworkResult
import app.domain.model.asEmptyDataNetworkResult
import app.domain.model.map
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.get
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import user.data.remote.dto.UserDto
import user.data.remote.mapper.toEntityModel
import user.domain.UserRepository
import user.domain.mapper.toDomainModel

class UserRepositoryImpl(
    private val httpClient: HttpClient,
    db: AppDatabase,
) : UserRepository {
    private val userDao = db.userDao()

    override suspend fun storeUser(): EmptyNetworkResult<NetworkError> {
        return safeCall<UserDto> {
            httpClient.get(urlString = constructUrl("/auth/me"))
        }.map { dto ->
            userDao.insertUser(dto.toEntityModel())
        }.asEmptyDataNetworkResult()
    }

    override fun getUser() = userDao.getUser().map { userWithDetails ->
        userWithDetails?.toDomainModel()
    }.distinctUntilChanged()

    override suspend fun getUserId() = userDao.getUserId()

    override suspend fun clearLocalUserSession() {
        userDao.clearUser()
        httpClient.authProvider<BearerAuthProvider>()?.clearToken()
    }
}
