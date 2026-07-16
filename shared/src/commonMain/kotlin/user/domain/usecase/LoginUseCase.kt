package user.domain.usecase

import app.data.local.SessionManager
import app.domain.NetworkError
import app.domain.model.EmptyNetworkResult
import app.domain.model.NetworkResult
import auth.domain.AuthRepository
import auth.domain.model.request.LoginRequest

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(request: LoginRequest): EmptyNetworkResult<NetworkError> {
        val result = authRepository.login(request)
        if (result is NetworkResult.Success) {
            sessionManager.onSessionChanged()
        }
        return result
    }
}
