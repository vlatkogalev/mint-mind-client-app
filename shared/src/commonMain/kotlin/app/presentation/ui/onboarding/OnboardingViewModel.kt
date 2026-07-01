package app.presentation.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.util.coreComponent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel : ViewModel() {
    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.asStateFlow()

    private val actionChannel = Channel<OnboardingScreenAction> { }
    val actions = actionChannel.receiveAsFlow()

    fun onScreenAction(event: OnboardingScreenAction) {
        when (event) {
            OnboardingScreenAction.OnNext -> goNext()
            OnboardingScreenAction.OnBack -> goBack()
            else -> Unit
        }
    }

    private fun goNext() {
        val currentPage = _state.value.currentPage
        if (currentPage < _state.value.totalPages - 1) {
            _state.update { it.copy(currentPage = currentPage + 1) }
        } else {
            completeOnboarding()
        }
    }

    private fun goBack() {
        val currentPage = _state.value.currentPage
        if (currentPage > 0) {
            _state.update { it.copy(currentPage = currentPage - 1) }
        }
    }

    fun completeOnboarding() = viewModelScope.launch {
        coreComponent.appPreferences.setOnboardingCompleted()
        actionChannel.send(OnboardingScreenAction.NavigateToHome)
    }

    private fun skipOnboarding() = viewModelScope.launch {
        actionChannel.send(OnboardingScreenAction.NavigateToHome)
    }
}
