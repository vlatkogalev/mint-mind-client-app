package app.data.local

import collections.domain.CollectionRepository

class SessionManager(
    private val collectionRepository: CollectionRepository,
    private val tokenManager: TokenManager,
) {
    suspend fun onSessionChanged() {
        collectionRepository.clearUserData()
        tokenManager.bumpSessionEpoch()
    }
}
