package auth.presentation.ui.reset_password


sealed interface ResetPasswordScreenEvent {
    data class EmailChanged(val email: String) : ResetPasswordScreenEvent
    data object Submit : ResetPasswordScreenEvent
}