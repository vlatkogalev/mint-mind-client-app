package auth.domain

import app.domain.NetworkError
import app.domain.model.EmptyNetworkResult
import auth.domain.model.request.CreateUserRequest
import auth.domain.model.request.LoginRequest
import auth.domain.model.request.ResendVerificationRequest
import auth.domain.model.request.ResetPasswordRequest

interface AuthRepository {
    /**
     * Ensures an anonymous session exists for this device.
     *
     * Resolves the permanent installationId via
     * `TokenManager.getOrCreateInstallationId`, creates the anonymous
     * session on the backend and persists the returned tokens.
     */
    suspend fun authenticateAnonymously(): EmptyNetworkResult<NetworkError>

    /**
     * Upgrades the current anonymous account to a registered one.
     *
     * Always goes through `POST /auth/upgrade-account`. The userId is preserved and the
     * returned tokens are persisted so the session stays valid while the email is verified.
     */
    suspend fun upgradeAccount(request: CreateUserRequest): EmptyNetworkResult<NetworkError>

    /**
     * Logs a registered user in. Always carries the installationId so the backend can
     * silently merge any anonymous data present on this device.
     */
    suspend fun login(request: LoginRequest): EmptyNetworkResult<NetworkError>

    suspend fun resendVerification(request: ResendVerificationRequest): EmptyNetworkResult<NetworkError>

    suspend fun resetPassword(request: ResetPasswordRequest): EmptyNetworkResult<NetworkError>

    /**
     * Server-side logout. Callers decide whether local cleanup may continue based on the result.
     */
    suspend fun logout(): EmptyNetworkResult<NetworkError>
}
