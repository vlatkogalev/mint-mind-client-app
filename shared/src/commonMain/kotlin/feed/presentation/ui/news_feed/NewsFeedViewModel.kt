package feed.presentation.ui.news_feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import feed.domain.FeedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NewsFeedViewModel(
    feedRepository: FeedRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NewsFeedState())
    val state = _state.asStateFlow()

    val postsPagingFlow = feedRepository.getFeed().cachedIn(viewModelScope)

    fun toggleRedirectDialog(redirectUri: String?) {
        _state.update {
            it.copy(
                showRedirectDialog = !state.value.showRedirectDialog,
                redirectUri = redirectUri
            )
        }
    }
}