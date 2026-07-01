package app.domain.model

sealed class ValidationEvent {
    data object Success : ValidationEvent()
    data object Error : ValidationEvent()
}