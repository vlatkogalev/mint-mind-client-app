package collections.presentation.ui.coin

sealed interface CoinScreenAction {
    data object NavigateUp : CoinScreenAction
}