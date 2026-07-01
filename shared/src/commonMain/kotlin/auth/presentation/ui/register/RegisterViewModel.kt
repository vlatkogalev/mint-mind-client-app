package auth.presentation.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.domain.model.ApiResponseEvent
import app.domain.model.ValidationEvent
import app.domain.model.onError
import app.domain.model.onSuccess
import app.domain.toErrorMessage
import app.domain.usecase.ValidateCommonFieldUseCase
import app.presentation.util.UiText
import auth.domain.AuthRepository
import auth.domain.model.request.CreateUserRequest
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.error_field_empty
import mintmind.shared.generated.resources.error_form_incomplete
import user.domain.usecase.ValidateConfirmEmailUseCase
import user.domain.usecase.ValidateConfirmPasswordUseCase
import user.domain.usecase.ValidateEmailUseCase
import user.domain.usecase.ValidatePasswordUseCase

class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validateConfirmEmailUseCase: ValidateConfirmEmailUseCase,
    private val validateCommonFieldUseCase: ValidateCommonFieldUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvent> { }
    val validationEvents = validationEventChannel.receiveAsFlow()

    private val apiResponseEventChannel = Channel<ApiResponseEvent> { }
    val apiResponseEvents = apiResponseEventChannel.receiveAsFlow()

    init {
        collectValidationEvents()
    }

    fun toggleConfirmDialog() {
        _state.update { it.copy(showConfirmDialog = !state.value.showConfirmDialog) }
    }

    private fun collectValidationEvents() = viewModelScope.launch {
        validationEvents.collect { event ->
            when (event) {
                ValidationEvent.Success -> upgradeAccount()
                ValidationEvent.Error -> apiResponseEventChannel.send(
                    ApiResponseEvent.Error(UiText.StaticResource(stringResource = Res.string.error_form_incomplete))
                )
            }
        }
    }

    fun onFormEvent(event: RegisterScreenEvent) {
        when (event) {
            is RegisterScreenEvent.EmailChanged -> {
                _state.update { state -> state.copy(email = event.email) }
            }

            is RegisterScreenEvent.ConfEmailChanged -> {
                _state.update { state -> state.copy(confEmail = event.confEmail) }
            }

            is RegisterScreenEvent.FirstNameChanged -> {
                _state.update { state -> state.copy(firstName = event.firstName) }
            }

            is RegisterScreenEvent.LastNameChanged -> {
                _state.update { state -> state.copy(lastName = event.lastName) }
            }

            is RegisterScreenEvent.PasswordChanged -> {
                _state.update { state -> state.copy(password = event.password) }
            }

            is RegisterScreenEvent.ConfPasswordChanged -> {
                _state.update { state -> state.copy(confPassword = event.confPassword) }
            }

            RegisterScreenEvent.Submit -> {
                submitFormData()
            }
        }
    }

    private fun submitFormData() {
        val emailResult = validateEmailUseCase(_state.value.email)
        val confEmailResult = validateConfirmEmailUseCase(
            _state.value.confEmail,
            _state.value.email
        )
        val firstNameResult = validateCommonFieldUseCase(
            input = _state.value.firstName,
            errorMessageResource = Res.string.error_field_empty
        )
        val lastNameResult = validateCommonFieldUseCase(
            input = _state.value.lastName,
            errorMessageResource = Res.string.error_field_empty
        )
        val passwordResult = validatePasswordUseCase(_state.value.password)
        val confPasswordResult = validateConfirmPasswordUseCase(
            _state.value.confPassword,
            _state.value.password
        )

        val hasError = listOf(
            emailResult,
            confEmailResult,
            firstNameResult,
            lastNameResult,
            passwordResult,
            confPasswordResult
        ).any { !it.successful }

        if (hasError) {
            _state.update { state ->
                state.copy(
                    emailError = emailResult.errorMessageResource,
                    confEmailError = confEmailResult.errorMessageResource,
                    firstNameError = firstNameResult.errorMessageResource,
                    lastNameError = lastNameResult.errorMessageResource,
                    passwordError = passwordResult.errorMessageResource,
                    confPasswordError = confPasswordResult.errorMessageResource
                )
            }

            viewModelScope.launch {
                validationEventChannel.send(ValidationEvent.Error)
            }

            return
        }

        _state.update { state ->
            state.copy(
                emailError = null,
                confEmailError = null,
                firstNameError = null,
                lastNameError = null,
                passwordError = null,
                confPasswordError = null
            )
        }

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    private fun upgradeAccount() = viewModelScope.launch {
        val request = CreateUserRequest(
            email = state.value.email,
            password = state.value.password,
            firstName = state.value.firstName,
            lastName = state.value.lastName,
        )

        _state.update { it.copy(isLoading = true) }
        authRepository.upgradeAccount(request)
            .onSuccess {
                _state.update { it.copy(isLoading = false) }
                apiResponseEventChannel.send(ApiResponseEvent.Success)
            }
            .onError { error ->
                _state.update { it.copy(isLoading = false) }
                apiResponseEventChannel.send(ApiResponseEvent.Error(error.toErrorMessage()))
            }
    }
}