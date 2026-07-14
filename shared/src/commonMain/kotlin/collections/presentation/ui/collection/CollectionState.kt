package collections.presentation.ui.collection

import collections.domain.model.Coin
import collections.domain.model.CoinSet
import collections.domain.model.CoinSetSortOption
import collections.domain.model.CoinSortOption
import collections.domain.model.CollectionScreenType
import collections.domain.model.CollectionStats

data class CollectionState(
    val isLoading: Boolean = false,
    val isCoinMultiSelectModeEnabled: Boolean = false,
    val isSetMultiSelectModeEnabled: Boolean = false,
    val selectedCoins: Set<Coin> = emptySet(),
    val selectedSets: Set<CoinSet> = emptySet(),
    val totalCollectionValue: Double = 0.0,
    val totalCoinCount: Int = 0,
    val totalIssuerCount: Int = 0,
    val highlights: List<CollectionStats.HighlightedCoin>? = null,
    val selectedScreenType: CollectionScreenType = CollectionScreenType.SUMMARY,
    val sets: List<CoinSet> = emptyList(),
    val showCreateSetDialog: Boolean = false,
    val showDeleteCoinsDialog: Boolean = false,
    val showDeleteSetsDialog: Boolean = false,
    val showMoveSheet: Boolean = false,
    val isProcessingBulkAction: Boolean = false,
    val coinSortOption: CoinSortOption = CoinSortOption.DEFAULT,
    val setSortOption: CoinSetSortOption = CoinSetSortOption.DEFAULT,
)
