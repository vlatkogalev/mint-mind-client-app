package app.domain.model

import app.presentation.util.UiText

sealed class ApiResponseEvent {
    data object Success : ApiResponseEvent()
    data class Error(val message: UiText) : ApiResponseEvent()
}