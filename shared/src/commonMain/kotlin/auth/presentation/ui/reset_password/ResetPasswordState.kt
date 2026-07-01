package auth.presentation.ui.reset_password

import org.jetbrains.compose.resources.StringResource

data class ResetPasswordState(
    val isLoading: Boolean = false,
    val showConfirmDialog: Boolean = false,
    val email: String = "",
    val emailError: StringResource? = null
)
