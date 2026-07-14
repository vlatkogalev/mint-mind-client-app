package collections.presentation.ui.collection

import app.domain.NetworkError

sealed interface CollectionEvent {
    data class CoinsDeleted(val count: Int) : CollectionEvent
    data class SetsDeleted(val count: Int) : CollectionEvent
    data class CoinsMoved(val count: Int, val setName: String) : CollectionEvent
    data class Error(val error: NetworkError) : CollectionEvent
    data class BulkActionBlocked(val reason: String) : CollectionEvent
}
