package collections.presentation.ui.collection

import app.domain.NetworkError

sealed interface CollectionScreenEvent {
    data class CoinsDeleted(val count: Int) : CollectionScreenEvent
    data class SetsDeleted(val count: Int) : CollectionScreenEvent
    data class CoinsMoved(val count: Int, val setName: String) : CollectionScreenEvent
    data class Error(val error: NetworkError) : CollectionScreenEvent
    data class BulkActionBlocked(val reason: String) : CollectionScreenEvent
}
