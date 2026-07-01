package feed.presentation.ui.news_feed

data class NewsFeedState(
    val showRedirectDialog: Boolean = false,
    val redirectUri: String? = null,
)
