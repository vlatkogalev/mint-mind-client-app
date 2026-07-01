package collections.presentation.ui.collection

import collections.domain.model.CollectionScreenType
import collections.domain.model.CollectionStats

data class CollectionState(
    val isLoading: Boolean = false,
    val isCoinMultiSelectModeEnabled: Boolean = false,
    val isSetMultiSelectModeEnabled: Boolean = false,
    val totalCollectionValue: Double = 0.0,
    val totalCoinCount: Int = 0,
    val totalIssuerCount: Int = 0,
    val highlights: List<CollectionStats.HighlightedCoin>? = null,
    val selectedScreenType: CollectionScreenType = CollectionScreenType.SUMMARY,
)
