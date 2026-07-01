package auth.presentation.ui.register

sealed interface RegisterScreenEvent {
    data class EmailChanged(val email: String) : RegisterScreenEvent
    data class ConfEmailChanged(val confEmail: String) : RegisterScreenEvent
    data class FirstNameChanged(val firstName: String) : RegisterScreenEvent
    data class LastNameChanged(val lastName: String) : RegisterScreenEvent
    data class PasswordChanged(val password: String) : RegisterScreenEvent
    data class ConfPasswordChanged(val confPassword: String) : RegisterScreenEvent

    data object Submit : RegisterScreenEvent
}