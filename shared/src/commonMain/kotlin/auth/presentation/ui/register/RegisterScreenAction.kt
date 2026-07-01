package auth.presentation.ui.register

sealed interface RegisterScreenAction {
    data object NavigateUp : RegisterScreenAction
    data object ToggleConfirmDialog : RegisterScreenAction
}