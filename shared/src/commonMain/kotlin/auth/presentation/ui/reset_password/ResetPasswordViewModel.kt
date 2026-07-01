package auth.presentation.ui.reset_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.domain.model.ApiResponseEvent
import app.domain.model.ValidationEvent
import app.domain.model.onError
import app.domain.model.onSuccess
import app.domain.toErrorMessage
import auth.domain.AuthRepository
import auth.domain.model.request.ResetPasswordRequest
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import user.domain.usecase.ValidateEmailUseCase

class ResetPasswordViewModel(
    private val authRepository: AuthRepository,
    private val validateEmailUseCase: ValidateEmailUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ResetPasswordState())
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

    fun onFormEvent(event: ResetPasswordScreenEvent) {
        when (event) {
            is ResetPasswordScreenEvent.EmailChanged -> {
                _state.update { state -> state.copy(email = event.email) }
            }

            ResetPasswordScreenEvent.Submit -> {
                submitFormData()
            }
        }
    }

    private fun collectValidationEvents() = viewModelScope.launch {
        validationEvents.collect { event ->
            when (event) {
                ValidationEvent.Success -> resetPassword()
                else -> Unit
            }
        }
    }

    private fun submitFormData() {
        val emailResult = validateEmailUseCase(_state.value.email)

        val hasError = listOf(
            emailResult
        ).any { !it.successful }

        if (hasError) {
            _state.update { state ->
                state.copy(
                    emailError = emailResult.errorMessageResource
                )
            }

            return
        }

        _state.update { state -> state.copy(emailError = null) }

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    private fun resetPassword() = viewModelScope.launch {
        val request = ResetPasswordRequest(
            email = state.value.email
        )

        _state.update { it.copy(isLoading = true) }
        authRepository.resetPassword(request)
            .onSuccess {
                _state.update {
                    it.copy(
                        isLoading = false,
                        showConfirmDialog = true
                    )
                }
            }
            .onError { error ->
                _state.update { it.copy(isLoading = false) }
                apiResponseEventChannel.send(ApiResponseEvent.Error(error.toErrorMessage()))
            }
    }
}