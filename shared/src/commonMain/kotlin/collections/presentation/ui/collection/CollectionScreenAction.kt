package collections.presentation.ui.collection

import collections.domain.model.CollectionScreenType

sealed interface CollectionScreenAction {
    data class NavigateToCoin(val coinId: String) : CollectionScreenAction
    data class NavigateToSet(val setId: String) : CollectionScreenAction
    data class ChangeScreenType(val screenType: CollectionScreenType) : CollectionScreenAction
    data object ShowCreateSetDialog : CollectionScreenAction
    data object DismissCreateSetDialog : CollectionScreenAction
    data class CreateSet(val name: String, val description: String?) : CollectionScreenAction
}