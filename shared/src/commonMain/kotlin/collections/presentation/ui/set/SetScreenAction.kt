package collections.presentation.ui.set

import collections.domain.model.Coin
import collections.domain.model.CoinSortOption

sealed interface SetScreenAction {
    data object NavigateUp : SetScreenAction
    data class NavigateToCoin(val coinId: String) : SetScreenAction
    data class ChangeCoinSort(val option: CoinSortOption) : SetScreenAction
    data object ToggleCoinMultiSelectMode : SetScreenAction
    data class ToggleCoinSelected(val coin: Coin) : SetScreenAction
    data object RequestRemoveSelectedFromSet : SetScreenAction
    data object ConfirmRemoveSelectedFromSet : SetScreenAction
    data object RequestDeleteSelectedCoins : SetScreenAction
    data object ConfirmDeleteSelectedCoins : SetScreenAction
    data object DismissDialog : SetScreenAction
}
