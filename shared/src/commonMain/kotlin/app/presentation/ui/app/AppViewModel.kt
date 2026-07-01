package app.presentation.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.domain.Language
import app.util.coreComponent
import auth.domain.AuthRepository
import dev.jordond.connectivity.Connectivity
import feed.domain.FeedRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class AppViewModel(
    private val connectivity: Connectivity,
    private val feedRepository: FeedRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private var lastFetchTime: Instant? = null
    private var dataJob: Job? = null

    // Single-flight guard while anonymous auth is actively running. Completed
    // failed attempts are cleared so future token-missing states can retry.
    private var anonymousSessionJob: Deferred<Unit>? = null

    private val _state = MutableStateFlow(AppState())
    val state = _state.asStateFlow()

    init {
        connectivity.start()

        viewModelScope.launch { ensureAnonymousSession().await() }
        getAppLanguage()
        getConnectivityStatus()
    }

    override fun onCleared() {
        super.onCleared()
        connectivity.stop()
    }

    private fun getConnectivityStatus() = viewModelScope.launch {
        connectivity.statusUpdates.collect { status ->
            when (status) {
                is Connectivity.Status.Connected -> {
                    _state.update { state -> state.copy(isDisconnected = false) }
                }

                is Connectivity.Status.Disconnected -> {
                    _state.update { state -> state.copy(isDisconnected = true) }
                }
            }
        }
    }

    private fun getAppLanguage() = viewModelScope.launch {
        coreComponent.appPreferences.getAppLanguage().collect { language ->
            _state.update { state -> state.copy(language = Language.fromValue(language)) }
        }
    }

    fun storeData() {
        val now = Clock.System.now()
        if (lastFetchTime != null && (now - lastFetchTime!!) < REFRESH_THRESHOLD) return

        dataJob?.cancel()
        dataJob = viewModelScope.launch {
            // Make sure a session (anonymous or registered) exists before any
            // authenticated request fires.
            ensureAnonymousSession().await()
            awaitAll(
                async { feedRepository.storeFeed() },
                async { feedRepository.storeCoinListings() }
            )
            lastFetchTime = Clock.System.now()
        }
    }

    /**
     * Guarantees the device has a valid session. Every user has an account from the moment
     * they first open the app — if no token is present yet, an anonymous session is created.
     * The permanent installationId is never touched here.
     */
    private fun ensureAnonymousSession(): Deferred<Unit> {
        anonymousSessionJob?.let { job ->
            if (job.isActive) return job
        }

        if (anonymousSessionJob?.isCompleted == true) {
            anonymousSessionJob = null
        }

        return viewModelScope.async {
            if (coreComponent.tokenManager.getToken() == null) {
                authRepository.authenticateAnonymously()
            }
            Unit
        }.also { anonymousSessionJob = it }
    }

    companion object {
        private val REFRESH_THRESHOLD = 1.minutes
    }
}