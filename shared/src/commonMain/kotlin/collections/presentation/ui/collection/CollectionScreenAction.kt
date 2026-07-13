package collections.presentation.ui.collection

import collections.domain.model.CollectionScreenType

sealed interface CollectionScreenAction {
    data class NavigateToCoin(val coinId: String) : CollectionScreenAction
    data class NavigateToSet(val setId: String) : CollectionScreenAction
    data class ChangeScreenType(val screenType: CollectionScreenType) : CollectionScreenAction
    data object ShowCreateSetDialog : CollectionScreenAction
    data object DismissCreateSetDialog : CollectionScreenAction
    data class CreateSet(val name: String, val description: String?) : CollectionScreenAction

    data object ToggleCoinMultiSelectMode : CollectionScreenAction
    data class ToggleCoinSelected(val coinId: String) : CollectionScreenAction
    data object DeleteSelectedCoins : CollectionScreenAction
    data object MoveSelectedCoins : CollectionScreenAction

    data object ToggleSetMultiSelectMode : CollectionScreenAction
    data class ToggleSetSelected(val setId: String) : CollectionScreenAction
    data object DeleteSelectedSets : CollectionScreenAction
}