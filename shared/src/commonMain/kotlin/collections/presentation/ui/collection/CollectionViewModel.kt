package collections.presentation.ui.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import app.domain.model.NetworkResult
import collections.domain.CollectionRepository
import collections.domain.model.CoinSetSortOption
import collections.domain.model.CoinSortOption
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CollectionViewModel(
    private val collectionRepository: CollectionRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(CollectionState())
    val state: StateFlow<CollectionState> = _state

    private val _events = Channel<CollectionEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val coinSort = MutableStateFlow(CoinSortOption.DEFAULT)
    val coinsPagingFlow = coinSort
        .flatMapLatest { collectionRepository.getCoins(sortBy = it) }
        .cachedIn(viewModelScope)

    private val setSort = MutableStateFlow(CoinSetSortOption.DEFAULT)

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

        setSort.flatMapLatest { collectionRepository.getSets(it) }
            .onEach { sets ->
                _state.update { it.copy(sets = sets) }
            }
            .launchIn(viewModelScope)

        setSort
            .onEach { collectionRepository.storeSets(it) }
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
            is CollectionScreenAction.NavigateToCoin -> Unit
            is CollectionScreenAction.NavigateToSet -> Unit

            is CollectionScreenAction.ChangeScreenType -> {
                _state.update { it.copy(selectedScreenType = action.screenType) }
            }

            is CollectionScreenAction.ChangeCoinSort -> {
                _state.update {
                    it.copy(
                        coinSortOption = action.option,
                        selectedCoins = emptySet(),
                    )
                }
                coinSort.value = action.option
            }

            is CollectionScreenAction.ChangeSetSort -> {
                _state.update {
                    it.copy(
                        setSortOption = action.option,
                        selectedSets = emptySet(),
                    )
                }
                setSort.value = action.option
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

            is CollectionScreenAction.RequestDeleteSelectedCoins -> {
                _state.update { it.copy(showDeleteCoinsDialog = true) }
            }

            is CollectionScreenAction.DismissDeleteDialog -> {
                _state.update {
                    it.copy(showDeleteCoinsDialog = false, showDeleteSetsDialog = false)
                }
            }

            is CollectionScreenAction.ConfirmDeleteSelectedCoins -> {
                val ids = _state.value.selectedCoins.map { it.id }
                if (ids.size > BULK_ACTION_LIMIT) {
                    _state.update { it.copy(showDeleteCoinsDialog = false) }
                    _events.trySend(
                        CollectionEvent.BulkActionBlocked(
                            "Cannot delete more than $BULK_ACTION_LIMIT items at once."
                        )
                    )
                    return
                }
                _state.update {
                    it.copy(showDeleteCoinsDialog = false, isProcessingBulkAction = true)
                }
                viewModelScope.launch {
                    when (val res = collectionRepository.deleteCoins(ids)) {
                        is NetworkResult.Success -> {
                            _state.update {
                                it.copy(
                                    isProcessingBulkAction = false,
                                    isCoinMultiSelectModeEnabled = false,
                                    selectedCoins = emptySet()
                                )
                            }
                            _events.send(CollectionEvent.CoinsDeleted(res.data.deleted))
                        }

                        is NetworkResult.Error -> {
                            _state.update { it.copy(isProcessingBulkAction = false) }
                            _events.send(CollectionEvent.Error(res.error))
                        }
                    }
                }
            }

            is CollectionScreenAction.ShowMoveSheet -> {
                _state.update { it.copy(showMoveSheet = true) }
            }

            is CollectionScreenAction.DismissMoveSheet -> {
                _state.update { it.copy(showMoveSheet = false) }
            }

            is CollectionScreenAction.MoveSelectedCoins -> {
                val ids = _state.value.selectedCoins.map { it.id }
                if (ids.size > BULK_ACTION_LIMIT) {
                    _state.update { it.copy(showMoveSheet = false) }
                    _events.trySend(
                        CollectionEvent.BulkActionBlocked(
                            "Cannot move more than $BULK_ACTION_LIMIT items at once."
                        )
                    )
                    return
                }
                val setName = _state.value.sets
                    .firstOrNull { it.id == action.targetSetId }?.name.orEmpty()
                _state.update {
                    it.copy(showMoveSheet = false, isProcessingBulkAction = true)
                }
                viewModelScope.launch {
                    when (val res =
                        collectionRepository.moveCoinsToSet(action.targetSetId, ids)) {
                        is NetworkResult.Success -> {
                            _state.update {
                                it.copy(
                                    isProcessingBulkAction = false,
                                    isCoinMultiSelectModeEnabled = false,
                                    selectedCoins = emptySet()
                                )
                            }
                            _events.send(CollectionEvent.CoinsMoved(ids.size, setName))
                        }

                        is NetworkResult.Error -> {
                            _state.update { it.copy(isProcessingBulkAction = false) }
                            _events.send(CollectionEvent.Error(res.error))
                        }
                    }
                }
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

            is CollectionScreenAction.RequestDeleteSelectedSets -> {
                _state.update { it.copy(showDeleteSetsDialog = true) }
            }

            is CollectionScreenAction.ConfirmDeleteSelectedSets -> {
                val ids = _state.value.selectedSets.map { it.id }
                if (ids.size > BULK_ACTION_LIMIT) {
                    _state.update { it.copy(showDeleteSetsDialog = false) }
                    _events.trySend(
                        CollectionEvent.BulkActionBlocked(
                            "Cannot delete more than $BULK_ACTION_LIMIT items at once."
                        )
                    )
                    return
                }
                _state.update {
                    it.copy(showDeleteSetsDialog = false, isProcessingBulkAction = true)
                }
                viewModelScope.launch {
                    when (val res = collectionRepository.deleteSets(ids)) {
                        is NetworkResult.Success -> {
                            _state.update {
                                it.copy(
                                    isProcessingBulkAction = false,
                                    isSetMultiSelectModeEnabled = false,
                                    selectedSets = emptySet()
                                )
                            }
                            _events.send(CollectionEvent.SetsDeleted(res.data.deleted))
                        }

                        is NetworkResult.Error -> {
                            _state.update { it.copy(isProcessingBulkAction = false) }
                            _events.send(CollectionEvent.Error(res.error))
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val BULK_ACTION_LIMIT = 200
    }
}