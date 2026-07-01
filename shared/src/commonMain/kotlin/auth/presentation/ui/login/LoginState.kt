package auth.presentation.ui.login

import org.jetbrains.compose.resources.StringResource

data class LoginState(
    val isLoading: Boolean = false,
    val email: String = "",
    val emailError: StringResource? = null,
    val password: String = "",
    val passwordError: StringResource? = null,
    val showVerificationDialog: Boolean = false,
    val isResendingVerification: Boolean = false,
)
