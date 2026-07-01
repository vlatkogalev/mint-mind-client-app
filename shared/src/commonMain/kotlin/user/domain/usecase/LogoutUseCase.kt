package user.domain.usecase

import app.data.local.TokenManager
import app.domain.NetworkError
import app.domain.model.onError
import app.domain.model.onSuccess
import auth.domain.AuthRepository
import user.domain.UserRepository

class LogoutUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(): LogoutResult {
        var result: LogoutResult = LogoutResult.Success

        authRepository.logout()
            .onSuccess {
                tokenManager.deleteTokens()
                userRepository.clearLocalUserSession()
                authRepository.authenticateAnonymously()
                    .onError { error ->
                        result = LogoutResult.AnonymousAuthFailed(error)
                    }
            }
            .onError { error ->
                result = LogoutResult.LogoutFailed(error)
            }

        return result
    }
}

sealed interface LogoutResult {
    data object Success : LogoutResult
    data class LogoutFailed(val error: NetworkError) : LogoutResult
    data class AnonymousAuthFailed(val error: NetworkError) : LogoutResult
}
