package app.presentation.ui.home

import feed.domain.model.CoinListing
import feed.domain.model.Post

data class HomeState(
    val isLoading: Boolean = false,
    val showRedirectDialog: Boolean = false,
    val redirectUri: String? = null,
    val posts: List<Post> = emptyList(),
    val coinListings: List<CoinListing> = emptyList(),
)
