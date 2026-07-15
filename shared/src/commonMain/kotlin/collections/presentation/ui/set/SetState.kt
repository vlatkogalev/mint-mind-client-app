package collections.presentation.ui.set

import collections.domain.model.Coin
import collections.domain.model.CoinSet
import collections.domain.model.CoinSortOption

data class SetState(
    val isLoading: Boolean = false,
    val set: CoinSet? = null,
    val coinSortOption: CoinSortOption = CoinSortOption.DEFAULT,
    val isCoinMultiSelectModeEnabled: Boolean = false,
    val selectedCoins: Set<Coin> = emptySet(),
    val isProcessingBulkAction: Boolean = false,
    val showRemoveFromSetDialog: Boolean = false,
    val showDeleteCoinsDialog: Boolean = false,
)
