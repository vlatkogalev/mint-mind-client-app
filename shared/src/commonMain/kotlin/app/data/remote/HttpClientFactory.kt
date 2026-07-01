package app.data.remote

import app.util.coreComponent
import auth.data.remote.dto.LoginDto
import auth.domain.model.request.RefreshTokenRequest
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.sse.SSE
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.milliseconds

internal fun createBaseHttpClient(
    json: Json,
    enableAuth: Boolean,
    authClient: HttpClient? = null
): HttpClient = HttpClient {
    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                Napier.v(tag = "HTTP Client", message = message)
            }
        }
    }

    install(ContentNegotiation) {
        json(json = json)
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 30000
        connectTimeoutMillis = 30000
        socketTimeoutMillis = 30000
    }

    if (enableAuth) {
        requireNotNull(authClient) { "authClient must be provided when enableAuth is true" }

        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = coreComponent.tokenManager.getToken()
                    val refreshToken = coreComponent.tokenManager.getRefreshToken()

                    if (accessToken.isNullOrEmpty()) {
                        null
                    } else {
                        BearerTokens(
                            accessToken = accessToken,
                            refreshToken = refreshToken.orEmpty()
                        )
                    }
                }

                refreshTokens {
                    val refreshToken = coreComponent.tokenManager.getRefreshToken()
                    if (refreshToken.isNullOrBlank()) {
                        Napier.w(tag = "Auth") { "No refresh token available" }
                        return@refreshTokens null
                    }

                    try {
                        Napier.d(tag = "Auth") { "Attempting token refresh" }

                        val tokensResponse = authClient.post(
                            urlString = constructUrl("/auth/refresh")
                        ) {
                            markAsRefreshTokenRequest()
                            contentType(ContentType.Application.Json)
                            setBody(RefreshTokenRequest(refreshToken = refreshToken))
                        }.body<LoginDto>()

                        coreComponent.tokenManager.saveTokens(
                            token = tokensResponse.accessToken,
                            refreshToken = tokensResponse.refreshToken
                        )

                        Napier.d(tag = "Auth") { "Token refresh successful" }

                        BearerTokens(
                            accessToken = tokensResponse.accessToken,
                            refreshToken = tokensResponse.refreshToken
                        )
                    } catch (e: Exception) {
                        Napier.e(tag = "Auth", throwable = e) {
                            "Token refresh failed: ${e.message}"
                        }

                        coreComponent.tokenManager.deleteTokens()
                        null
                    }
                }
            }
        }
    }

    defaultRequest {
        contentType(ContentType.Application.Json)
    }
}

internal fun createSSEHttpClient(): HttpClient = HttpClient {
    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                Napier.v(tag = "SSE HTTP Client", message = message)
            }
        }
    }

    install(SSE) {
        reconnectionTime = 3000.milliseconds
        maxReconnectionAttempts = Int.MAX_VALUE
        showRetryEvents()
        showCommentEvents()
    }

    install(HttpTimeout) {
        connectTimeoutMillis = 10000
        requestTimeoutMillis = HttpTimeoutConfig.INFINITE_TIMEOUT_MS
        socketTimeoutMillis = HttpTimeoutConfig.INFINITE_TIMEOUT_MS
    }
}

internal fun createS3HttpClient(): HttpClient = HttpClient {
    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                Napier.v(tag = "S3 HTTP Client", message = message)
            }
        }
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 60_000
        connectTimeoutMillis = 15_000
        socketTimeoutMillis = 60_000
    }
}