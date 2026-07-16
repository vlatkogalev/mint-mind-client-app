package auth.data

import app.data.remote.constructUrl
import app.data.remote.dto.MessageResponse
import app.data.remote.safeCall
import app.domain.NetworkError
import app.domain.model.EmptyNetworkResult
import app.domain.model.NetworkResult
import app.domain.model.asEmptyDataNetworkResult
import app.util.coreComponent
import auth.data.remote.dto.LoginDto
import auth.domain.AuthRepository
import auth.domain.model.request.CreateAnonymousUserRequest
import auth.domain.model.request.CreateUserRequest
import auth.domain.model.request.LoginRequest
import auth.domain.model.request.ResendVerificationRequest
import auth.domain.model.request.ResetPasswordRequest
import collections.domain.CollectionRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthRepositoryImpl(
    private val authClient: HttpClient,
    private val httpClient: HttpClient,
    private val collectionRepository: CollectionRepository,
) : AuthRepository {

    override suspend fun authenticateAnonymously(): EmptyNetworkResult<NetworkError> {
        val installationId = coreComponent.tokenManager.getOrCreateInstallationId()
        val request = CreateAnonymousUserRequest(installationId = installationId)

        return safeCall<LoginDto> {
            authClient.post(urlString = constructUrl("/auth/anonymous")) {
                setBody(request)
            }
        }.persistTokens()
    }

    override suspend fun upgradeAccount(request: CreateUserRequest): EmptyNetworkResult<NetworkError> {
        val result = safeCall<LoginDto> {
            httpClient.post(urlString = constructUrl("/auth/upgrade-account")) {
                setBody(request)
            }
        }.persistTokens()
        if (result is NetworkResult.Success) {
            collectionRepository.clearUserData()
        }
        return result
    }

    override suspend fun login(request: LoginRequest): EmptyNetworkResult<NetworkError> {
        val installationId = coreComponent.tokenManager.getOrCreateInstallationId()
        val authenticatedRequest = request.copy(installationId = installationId)

        val result = safeCall<LoginDto> {
            authClient.post(urlString = constructUrl("/auth/login")) {
                setBody(authenticatedRequest)
            }
        }.persistTokens()
        if (result is NetworkResult.Success) {
            collectionRepository.clearUserData()
        }
        return result
    }

    override suspend fun resendVerification(request: ResendVerificationRequest): EmptyNetworkResult<NetworkError> {
        return safeCall<MessageResponse> {
            authClient.post(urlString = constructUrl("/auth/resend-verification")) {
                setBody(request)
            }
        }.asEmptyDataNetworkResult()
    }

    override suspend fun resetPassword(request: ResetPasswordRequest): EmptyNetworkResult<NetworkError> {
        return safeCall<MessageResponse> {
            authClient.post(urlString = constructUrl("/auth/password-reset/request")) {
                setBody(request)
            }
        }.asEmptyDataNetworkResult()
    }

    override suspend fun logout(): EmptyNetworkResult<NetworkError> {
        return safeCall<MessageResponse> {
            httpClient.post(urlString = constructUrl("/auth/logout"))
        }.asEmptyDataNetworkResult()
    }

    private suspend fun NetworkResult<LoginDto, NetworkError>.persistTokens(): EmptyNetworkResult<NetworkError> {
        return when (this) {
            is NetworkResult.Error -> NetworkResult.Error(error)
            is NetworkResult.Success -> {
                coreComponent.tokenManager.saveTokens(
                    token = data.accessToken,
                    refreshToken = data.refreshToken
                )
                // Drop any cached bearer token so the next request reloads from TokenManager.
                httpClient.authProvider<BearerAuthProvider>()?.clearToken()
                NetworkResult.Success(Unit)
            }
        }.asEmptyDataNetworkResult()
    }
}