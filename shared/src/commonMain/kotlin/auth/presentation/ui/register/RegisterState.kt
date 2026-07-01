package auth.presentation.ui.register

import org.jetbrains.compose.resources.StringResource

data class RegisterState(
    val isLoading: Boolean = false,
    val showConfirmDialog: Boolean = false,
    val email: String = "",
    val emailError: StringResource? = null,
    val confEmail: String = "",
    val confEmailError: StringResource? = null,
    val firstName: String = "",
    val firstNameError: StringResource? = null,
    val lastName: String = "",
    val lastNameError: StringResource? = null,
    val password: String = "",
    val passwordError: StringResource? = null,
    val confPassword: String = "",
    val confPasswordError: StringResource? = null
)