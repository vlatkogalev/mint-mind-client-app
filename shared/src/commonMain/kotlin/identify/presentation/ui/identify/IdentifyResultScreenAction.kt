package identify.presentation.ui.identify

sealed interface IdentifyResultScreenAction {
    data object NavigateUp : IdentifyResultScreenAction
    data object SaveToCollection : IdentifyResultScreenAction
}
