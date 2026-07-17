package collections.presentation.ui.coin

sealed interface CoinScreenAction {
    data object NavigateUp : CoinScreenAction
    data object ShowMoveSheet : CoinScreenAction
    data object DismissMoveSheet : CoinScreenAction
    data class MoveToSet(val targetSetId: String) : CoinScreenAction
}