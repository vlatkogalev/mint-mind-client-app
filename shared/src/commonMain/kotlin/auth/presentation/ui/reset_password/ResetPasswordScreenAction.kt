package auth.presentation.ui.reset_password

sealed interface ResetPasswordScreenAction {
    data object NavigateUp : ResetPasswordScreenAction
    data object ToggleConfirmDialog : ResetPasswordScreenAction
}