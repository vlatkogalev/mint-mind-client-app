package auth.presentation.ui.login

sealed interface LoginScreenAction {
    data object NavigateUp : LoginScreenAction
    data object NavigateToRegister : LoginScreenAction
    data object NavigateToResetPassword : LoginScreenAction
}