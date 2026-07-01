package auth.presentation.ui.login

sealed interface LoginScreenEvent {
    data class EmailChanged(val email: String) : LoginScreenEvent
    data class PasswordChanged(val password: String) : LoginScreenEvent
    data object Submit : LoginScreenEvent
    data object ResendVerification : LoginScreenEvent
    data object DismissVerificationDialog : LoginScreenEvent
}
