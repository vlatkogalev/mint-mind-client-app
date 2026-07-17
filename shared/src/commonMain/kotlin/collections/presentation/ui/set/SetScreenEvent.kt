package collections.presentation.ui.set

import app.domain.NetworkError

sealed interface SetScreenEvent {
    data class CoinsRemovedFromSet(val count: Int) : SetScreenEvent
    data class CoinsDeleted(val count: Int) : SetScreenEvent
    data object SetDeleted : SetScreenEvent
    data class Error(val error: NetworkError) : SetScreenEvent
    data class BulkActionBlocked(val reason: String) : SetScreenEvent
    data object RefreshCoins : SetScreenEvent
}
