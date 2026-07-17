package collections.presentation.ui.coin

import app.domain.NetworkError

sealed interface CoinScreenEvent {
    data class CoinMoved(val count: Int, val setName: String) : CoinScreenEvent
    data class Error(val error: NetworkError) : CoinScreenEvent
}
