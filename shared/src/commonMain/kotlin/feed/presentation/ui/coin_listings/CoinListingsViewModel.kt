package feed.presentation.ui.coin_listings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import feed.domain.FeedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CoinListingsViewModel(
    feedRepository: FeedRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CoinListingsState())
    val state = _state.asStateFlow()

    val coinListingsPagingFlow = feedRepository.getCoinListings().cachedIn(viewModelScope)

    fun toggleRedirectDialog(redirectUri: String?) {
        _state.update {
            it.copy(
                showRedirectDialog = !state.value.showRedirectDialog,
                redirectUri = redirectUri
            )
        }
    }
}