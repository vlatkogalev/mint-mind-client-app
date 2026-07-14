package collections.presentation.ui.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import collections.domain.CollectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
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
                _state.update {
                    it.copy(
                        totalCollectionValue = stats?.estimatedTotalValueMean ?: 0.0,
                        totalCoinCount = stats?.totalCoins ?: 0,
                        totalIssuerCount = stats?.totalIssuers ?: 0,
                        highlights = stats?.highlights,
                    )
                }
            }
            .launchIn(viewModelScope)

        collectionRepository.getSets()
            .onEach { sets ->
                _state.update { it.copy(sets = sets) }
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            collectionRepository.storeCollectionStats()
        }

        viewModelScope.launch {
            collectionRepository.storeSets()
        }
    }

    fun onScreenAction(action: CollectionScreenAction) {
        when (action) {
            is CollectionScreenAction.ChangeScreenType -> {
                _state.update { it.copy(selectedScreenType = action.screenType) }
            }

            is CollectionScreenAction.ShowCreateSetDialog -> {
                _state.update { it.copy(showCreateSetDialog = true) }
            }

            is CollectionScreenAction.DismissCreateSetDialog -> {
                _state.update { it.copy(showCreateSetDialog = false) }
            }

            is CollectionScreenAction.CreateSet -> {
                _state.update { it.copy(showCreateSetDialog = false) }
                viewModelScope.launch {
                    collectionRepository.createSet(action.name, action.description)
                }
            }

            is CollectionScreenAction.ToggleCoinMultiSelectMode -> {
                _state.update { current ->
                    val enabled = !current.isCoinMultiSelectModeEnabled
                    current.copy(
                        isCoinMultiSelectModeEnabled = enabled,
                        selectedCoins = if (enabled) current.selectedCoins else emptySet(),
                    )
                }
            }

            is CollectionScreenAction.ToggleCoinSelected -> {
                _state.update { current ->
                    val updated = if (action.coin in current.selectedCoins) {
                        current.selectedCoins - action.coin
                    } else {
                        current.selectedCoins + action.coin
                    }
                    current.copy(selectedCoins = updated)
                }
            }

            is CollectionScreenAction.DeleteSelectedCoins -> {
                // TODO: wire to data layer (delete selectedCoins)
                _state.update {
                    it.copy(isCoinMultiSelectModeEnabled = false, selectedCoins = emptySet())
                }
            }

            is CollectionScreenAction.MoveSelectedCoins -> {
                // TODO: wire to data layer (move selectedCoins to a set)
            }

            is CollectionScreenAction.ToggleSetMultiSelectMode -> {
                _state.update { current ->
                    val enabled = !current.isSetMultiSelectModeEnabled
                    current.copy(
                        isSetMultiSelectModeEnabled = enabled,
                        selectedSets = if (enabled) current.selectedSets else emptySet(),
                    )
                }
            }

            is CollectionScreenAction.ToggleSetSelected -> {
                _state.update { current ->
                    val updated = if (action.set in current.selectedSets) {
                        current.selectedSets - action.set
                    } else {
                        current.selectedSets + action.set
                    }
                    current.copy(selectedSets = updated)
                }
            }

            is CollectionScreenAction.DeleteSelectedSets -> {
                // TODO: wire to data layer (delete selectedSets)
                _state.update {
                    it.copy(isSetMultiSelectModeEnabled = false, selectedSets = emptySet())
                }
            }

            else -> Unit
        }
    }
}
