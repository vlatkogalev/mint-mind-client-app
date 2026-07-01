package app.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import feed.domain.FeedRepository
import feed.domain.model.CoinListing
import feed.domain.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val feedRepository: FeedRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        setupStateFlow()
    }

    private fun setupStateFlow() = viewModelScope.launch {
        @Suppress("UNCHECKED_CAST")
        combine(
            feedRepository.getLatestPosts(),
            feedRepository.getLatestCoinListings(),
        ) { flows: Array<Any?> ->
            val feedItems = flows[0] as List<Post>
            val coinListings = flows[1] as List<CoinListing>

            HomeState(
                posts = feedItems,
                coinListings = coinListings
            )
        }.collect { updates ->
            _state.update { currentState ->
                currentState.copy(
                    posts = updates.posts,
                    coinListings = updates.coinListings
                )
            }
        }
    }

    fun toggleRedirectDialog(redirectUri: String?) {
        _state.update {
            it.copy(
                showRedirectDialog = !state.value.showRedirectDialog,
                redirectUri = redirectUri
            )
        }
    }
}