package collections.presentation.ui.collection

import collections.domain.model.Coin
import collections.domain.model.CoinSet
import collections.domain.model.CollectionScreenType

sealed interface CollectionScreenAction {
    data class NavigateToCoin(val coinId: String) : CollectionScreenAction
    data class NavigateToSet(val setId: String) : CollectionScreenAction
    data class ChangeScreenType(val screenType: CollectionScreenType) : CollectionScreenAction
    data object ShowCreateSetDialog : CollectionScreenAction
    data object DismissCreateSetDialog : CollectionScreenAction
    data class CreateSet(val name: String, val description: String?) : CollectionScreenAction

    data object ToggleCoinMultiSelectMode : CollectionScreenAction
    data class ToggleCoinSelected(val coin: Coin) : CollectionScreenAction

    data object RequestDeleteSelectedCoins : CollectionScreenAction
    data object ConfirmDeleteSelectedCoins : CollectionScreenAction
    data object DismissDeleteDialog : CollectionScreenAction

    data object ShowMoveSheet : CollectionScreenAction
    data object DismissMoveSheet : CollectionScreenAction
    data class MoveSelectedCoins(val targetSetId: String) : CollectionScreenAction

    data object ToggleSetMultiSelectMode : CollectionScreenAction
    data class ToggleSetSelected(val set: CoinSet) : CollectionScreenAction

    data object RequestDeleteSelectedSets : CollectionScreenAction
    data object ConfirmDeleteSelectedSets : CollectionScreenAction
}
