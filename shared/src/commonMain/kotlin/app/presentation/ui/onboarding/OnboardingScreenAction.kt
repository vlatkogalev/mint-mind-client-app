package app.presentation.ui.onboarding

sealed interface OnboardingScreenAction {
    data object NavigateToLogin : OnboardingScreenAction
    data object NavigateToHome : OnboardingScreenAction
    data object OnNext : OnboardingScreenAction
    data object OnBack : OnboardingScreenAction
}