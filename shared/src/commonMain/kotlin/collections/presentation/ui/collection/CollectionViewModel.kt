package collections.presentation.ui.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import app.domain.NetworkError
import app.domain.model.NetworkResult
import collections.domain.CollectionRepository
import collections.domain.model.Coin
import collections.domain.model.CoinSet
import collections.domain.model.CoinSetSortOption
import collections.domain.model.CoinSortOption
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val state: StateFlow<CollectionState> = _state.asStateFlow()

    private val _events = Channel<CollectionScreenEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val coinSort = MutableStateFlow(CoinSortOption.DEFAULT)
    val coinsPagingFlow = coinSort
        .flatMapLatest { collectionRepository.getCoins(sortBy = it) }
        .cachedIn(viewModelScope)

    private val setSort = MutableStateFlow(CoinSetSortOption.DEFAULT)

    init {
        observeCollectionStats()
        observeSets()
        refreshSets()

        viewModelScope.launch { collectionRepository.storeCollectionStats() }
    }

    fun onScreenResumed() {
        viewModelScope.launch { collectionRepository.storeCollectionStats() }
        viewModelScope.launch { collectionRepository.storeSets(setSort.value) }
    }

    private fun observeCollectionStats() {
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
    }

    private fun observeSets() {
        setSort
            .flatMapLatest { collectionRepository.getSets(it) }
            .onEach { sets -> _state.update { it.copy(sets = sets) } }
            .launchIn(viewModelScope)
    }

    private fun refreshSets() {
        setSort
            .onEach { collectionRepository.storeSets(it) }
            .launchIn(viewModelScope)
    }

    fun onScreenAction(action: CollectionScreenAction) {
        when (action) {
            is CollectionScreenAction.NavigateToCoin,

            is CollectionScreenAction.NavigateToSet -> Unit

            is CollectionScreenAction.ChangeScreenType ->
                _state.update { it.copy(selectedScreenType = action.screenType) }

            is CollectionScreenAction.ChangeCoinSort -> changeCoinSort(action.option)

            is CollectionScreenAction.ChangeSetSort -> changeSetSort(action.option)

            is CollectionScreenAction.ShowCreateSetDialog ->
                _state.update { it.copy(showCreateSetDialog = true) }

            is CollectionScreenAction.DismissCreateSetDialog ->
                _state.update { it.copy(showCreateSetDialog = false) }

            is CollectionScreenAction.CreateSet -> createSet(action.name, action.description)

            is CollectionScreenAction.ToggleCoinMultiSelectMode -> toggleCoinMultiSelect()

            is CollectionScreenAction.ToggleCoinSelected -> toggleCoinSelected(action.coin)

            is CollectionScreenAction.RequestDeleteSelectedCoins ->
                _state.update { it.copy(showDeleteCoinsDialog = true) }

            is CollectionScreenAction.DismissDeleteDialog ->
                _state.update {
                    it.copy(showDeleteCoinsDialog = false, showDeleteSetsDialog = false)
                }

            is CollectionScreenAction.ConfirmDeleteSelectedCoins -> deleteSelectedCoins()

            is CollectionScreenAction.ShowMoveSheet ->
                _state.update { it.copy(showMoveSheet = true) }

            is CollectionScreenAction.DismissMoveSheet ->
                _state.update { it.copy(showMoveSheet = false) }

            is CollectionScreenAction.MoveSelectedCoins -> moveSelectedCoins(action.targetSetId)

            is CollectionScreenAction.ToggleSetMultiSelectMode -> toggleSetMultiSelect()

            is CollectionScreenAction.ToggleSetSelected -> toggleSetSelected(action.set)

            is CollectionScreenAction.RequestDeleteSelectedSets ->
                _state.update { it.copy(showDeleteSetsDialog = true) }

            is CollectionScreenAction.ConfirmDeleteSelectedSets -> deleteSelectedSets()
        }
    }

    private fun changeCoinSort(option: CoinSortOption) {
        _state.update { it.copy(coinSortOption = option, selectedCoins = emptySet()) }
        coinSort.value = option
    }

    private fun changeSetSort(option: CoinSetSortOption) {
        _state.update { it.copy(setSortOption = option, selectedSets = emptySet()) }
        setSort.value = option
    }

    private fun createSet(name: String, description: String?) {
        _state.update { it.copy(showCreateSetDialog = false) }
        viewModelScope.launch {
            collectionRepository.createSet(name, description)
        }
    }

    private fun toggleCoinMultiSelect() {
        _state.update { current ->
            val enabled = !current.isCoinMultiSelectModeEnabled
            current.copy(
                isCoinMultiSelectModeEnabled = enabled,
                selectedCoins = if (enabled) current.selectedCoins else emptySet(),
            )
        }
    }

    private fun toggleCoinSelected(coin: Coin) {
        _state.update { current ->
            current.copy(
                selectedCoins = current.selectedCoins.toggle(coin)
            )
        }
    }

    private fun toggleSetMultiSelect() {
        _state.update { current ->
            val enabled = !current.isSetMultiSelectModeEnabled
            current.copy(
                isSetMultiSelectModeEnabled = enabled,
                selectedSets = if (enabled) current.selectedSets else emptySet(),
            )
        }
    }

    private fun toggleSetSelected(set: CoinSet) {
        _state.update { current ->
            current.copy(
                selectedSets = current.selectedSets.toggle(set)
            )
        }
    }

    private fun <T> Set<T>.toggle(item: T): Set<T> =
        if (item in this) this - item else this + item

    private fun deleteSelectedCoins() {
        val ids = _state.value.selectedCoins.map { it.id }
        if (rejectOversizedBulkAction(ids) { it.copy(showDeleteCoinsDialog = false) }) return

        _state.update { it.copy(showDeleteCoinsDialog = false, isProcessingBulkAction = true) }
        viewModelScope.launch {
            handleBulkResult(
                result = collectionRepository.deleteCoins(ids),
                clearSelection = {
                    it.copy(isCoinMultiSelectModeEnabled = false, selectedCoins = emptySet())
                },
                onSuccess = { CollectionScreenEvent.CoinsDeleted(it.deleted) },
            )
        }
    }

    private fun moveSelectedCoins(targetSetId: String) {
        val ids = _state.value.selectedCoins.map { it.id }
        if (rejectOversizedBulkAction(ids) { it.copy(showMoveSheet = false) }) return

        val setName = _state.value.sets.firstOrNull { it.id == targetSetId }?.name.orEmpty()
        _state.update { it.copy(showMoveSheet = false, isProcessingBulkAction = true) }
        viewModelScope.launch {
            handleBulkResult(
                result = collectionRepository.moveCoinsToSet(targetSetId, ids),
                clearSelection = {
                    it.copy(isCoinMultiSelectModeEnabled = false, selectedCoins = emptySet())
                },
                onSuccess = { CollectionScreenEvent.CoinsMoved(ids.size, setName) },
            )
        }
    }

    private fun deleteSelectedSets() {
        val ids = _state.value.selectedSets.map { it.id }
        if (rejectOversizedBulkAction(ids) { it.copy(showDeleteSetsDialog = false) }) return

        _state.update { it.copy(showDeleteSetsDialog = false, isProcessingBulkAction = true) }
        viewModelScope.launch {
            handleBulkResult(
                result = collectionRepository.deleteSets(ids),
                clearSelection = {
                    it.copy(isSetMultiSelectModeEnabled = false, selectedSets = emptySet())
                },
                onSuccess = { CollectionScreenEvent.SetsDeleted(it.deleted) },
            )
        }
    }

    /** Returns true if the action was rejected (and the caller should bail out). */
    private fun rejectOversizedBulkAction(
        ids: List<String>,
        dismissDialog: (CollectionState) -> CollectionState,
    ): Boolean {
        if (ids.size <= BULK_ACTION_LIMIT) return false
        _state.update(dismissDialog)
        _events.trySend(CollectionScreenEvent.BulkActionBlocked("Cannot delete more than $BULK_ACTION_LIMIT items at once."))
        return true
    }

    private suspend fun <T> handleBulkResult(
        result: NetworkResult<T, NetworkError>,
        clearSelection: (CollectionState) -> CollectionState,
        onSuccess: (T) -> CollectionScreenEvent,
    ) {
        when (result) {
            is NetworkResult.Success -> {
                _state.update { clearSelection(it).copy(isProcessingBulkAction = false) }
                _events.send(onSuccess(result.data))
            }

            is NetworkResult.Error -> {
                _state.update { it.copy(isProcessingBulkAction = false) }
                _events.send(CollectionScreenEvent.Error(result.error))
            }
        }
    }

    companion object {
        private const val BULK_ACTION_LIMIT = 200
    }
}