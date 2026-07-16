package user.domain.usecase

import app.data.local.TokenManager
import app.domain.NetworkError
import app.domain.model.NetworkResult
import app.domain.model.onError
import auth.domain.AuthRepository
import collections.domain.CollectionRepository
import user.domain.UserRepository

class LogoutUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val collectionRepository: CollectionRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(): LogoutResult {
        val serverResult = authRepository.logout()

        tokenManager.deleteTokens()
        userRepository.clearLocalUserSession()
        collectionRepository.clearUserData()

        var result: LogoutResult = when (serverResult) {
            is NetworkResult.Success -> LogoutResult.Success
            is NetworkResult.Error -> LogoutResult.LogoutFailed(serverResult.error)
        }

        authRepository.authenticateAnonymously()
            .onError { error ->
                result = LogoutResult.AnonymousAuthFailed(error)
            }

        tokenManager.bumpSessionEpoch()

        return result
    }
}

sealed interface LogoutResult {
    data object Success : LogoutResult
    data class LogoutFailed(val error: NetworkError) : LogoutResult
    data class AnonymousAuthFailed(val error: NetworkError) : LogoutResult
}
