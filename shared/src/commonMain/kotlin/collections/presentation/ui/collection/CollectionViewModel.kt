package collections.presentation.ui.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import collections.domain.CollectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CollectionViewModel(
    private val collectionRepository: CollectionRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(CollectionState())
    val state: StateFlow<CollectionState> = _state

    val coinsPagingFlow = collectionRepository.getCoins().cachedIn(viewModelScope)

    init {
        collectionRepository.getCollectionStats()
            .onEach { stats ->
                _state.value = _state.value.copy(
                    totalCollectionValue = stats?.estimatedTotalValueMean ?: 0.0,
                    totalCoinCount = stats?.totalCoins ?: 0,
                    totalIssuerCount = stats?.totalIssuers ?: 0,
                    highlights = stats?.highlights,
                )
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            collectionRepository.storeCollectionStats()
        }
    }

    fun onScreenAction(action: CollectionScreenAction) {
        when (action) {
            is CollectionScreenAction.ChangeScreenType -> {
                _state.value = _state.value.copy(selectedScreenType = action.screenType)
            }

            else -> Unit
        }
    }
}