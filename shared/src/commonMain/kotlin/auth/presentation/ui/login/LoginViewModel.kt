package auth.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.domain.NetworkError
import app.domain.model.ApiResponseEvent
import app.domain.model.ValidationEvent
import app.domain.model.onError
import app.domain.model.onSuccess
import app.domain.toErrorMessage
import app.domain.usecase.ValidateCommonFieldUseCase
import app.presentation.util.UiText
import auth.domain.AuthRepository
import auth.domain.model.request.LoginRequest
import auth.domain.model.request.ResendVerificationRequest
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.error_field_empty
import mintmind.shared.generated.resources.login_verification_resent
import user.domain.UserRepository

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val validateCommonFieldUseCase: ValidateCommonFieldUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvent> { }
    val validationEvents = validationEventChannel.receiveAsFlow()

    private val apiResponseEventChannel = Channel<ApiResponseEvent> { }
    val apiResponseEvents = apiResponseEventChannel.receiveAsFlow()

    private val messageChannel = Channel<UiText> { }
    val messages = messageChannel.receiveAsFlow()

    init {
        collectValidationEvents()
    }

    fun onFormEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.EmailChanged -> {
                _state.update { state -> state.copy(email = event.email) }
            }

            is LoginScreenEvent.PasswordChanged -> {
                _state.update { state -> state.copy(password = event.password) }
            }

            LoginScreenEvent.Submit -> {
                submitFormData()
            }

            LoginScreenEvent.ResendVerification -> {
                resendVerification()
            }

            LoginScreenEvent.DismissVerificationDialog -> {
                _state.update { it.copy(showVerificationDialog = false) }
            }
        }
    }

    private fun collectValidationEvents() = viewModelScope.launch {
        validationEvents.collect { event ->
            when (event) {
                ValidationEvent.Success -> login()
                else -> Unit
            }
        }
    }

    private fun login() = viewModelScope.launch {
        val request = LoginRequest(
            email = state.value.email,
            password = state.value.password,
            installationId = null // the repository attaches the real installationId
        )

        _state.update { it.copy(isLoading = true) }
        authRepository.login(request)
            .onSuccess {
                storeUser()
            }
            .onError { error ->
                _state.update { it.copy(isLoading = false) }
                if (error.isEmailVerificationRequired()) {
                    _state.update { it.copy(showVerificationDialog = true) }
                } else {
                    apiResponseEventChannel.send(ApiResponseEvent.Error(error.toErrorMessage()))
                }
            }
    }

    private suspend fun storeUser() = userRepository.storeUser()
        .onSuccess {
            _state.update { it.copy(isLoading = false) }
            apiResponseEventChannel.send(ApiResponseEvent.Success)
        }
        .onError { error ->
            _state.update { it.copy(isLoading = false) }
            apiResponseEventChannel.send(ApiResponseEvent.Error(error.toErrorMessage()))
        }

    private fun resendVerification() = viewModelScope.launch {
        _state.update { it.copy(isResendingVerification = true) }

        val request = ResendVerificationRequest(email = state.value.email)
        authRepository.resendVerification(request)
            .onSuccess {
                _state.update {
                    it.copy(isResendingVerification = false, showVerificationDialog = false)
                }
                messageChannel.send(UiText.StaticResource(Res.string.login_verification_resent))
            }
            .onError { error ->
                // The backend enforces a cooldown and returns a clear message with the
                // remaining wait time — surface it as-is.
                _state.update { it.copy(isResendingVerification = false) }
                apiResponseEventChannel.send(ApiResponseEvent.Error(error.toErrorMessage()))
            }
    }

    private fun submitFormData() {
        val emailResult = validateCommonFieldUseCase(
            input = state.value.email,
            errorMessageResource = Res.string.error_field_empty
        )
        val passwordResult = validateCommonFieldUseCase(
            input = state.value.password,
            errorMessageResource = Res.string.error_field_empty
        )

        val hasError = listOf(
            emailResult,
            passwordResult
        ).any { !it.successful }

        if (hasError) {
            _state.update { state ->
                state.copy(
                    emailError = emailResult.errorMessageResource,
                    passwordError = passwordResult.errorMessageResource
                )
            }

            return
        }

        _state.update { state -> state.copy(emailError = null, passwordError = null) }

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    private fun NetworkError.isEmailVerificationRequired(): Boolean {
        return this is NetworkError.ApiError &&
                message?.contains("verif", ignoreCase = true) == true
    }
}
