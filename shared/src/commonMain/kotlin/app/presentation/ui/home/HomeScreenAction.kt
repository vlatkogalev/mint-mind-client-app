package app.presentation.ui.home


sealed interface HomeScreenAction {
    object NavigateToUser : HomeScreenAction
    object NavigateToIdentify : HomeScreenAction
    object NavigateToCoinListings : HomeScreenAction
    object NavigateToNewsFeed : HomeScreenAction
    data class ToggleRedirectDialog(val redirectUri: String?) : HomeScreenAction
}