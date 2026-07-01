package feed.presentation.ui.news_feed

sealed interface NewsFeedScreenAction {
    data object NavigateUp : NewsFeedScreenAction
    data class ToggleRedirectDialog(val redirectUri: String?) : NewsFeedScreenAction
    data class LoadPost(val url: String?) : NewsFeedScreenAction
}