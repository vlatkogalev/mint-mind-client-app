package collections.presentation.ui.set

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import app.domain.NetworkError
import app.domain.model.NetworkResult
import collections.domain.CollectionRepository
import collections.domain.model.Coin
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
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class SetViewModel(
    private val setId: String,
    private val collectionRepository: CollectionRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(SetState())
    val state: StateFlow<SetState> = _state.asStateFlow()

    private val _events = Channel<SetScreenEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val coinSort = MutableStateFlow(CoinSortOption.DEFAULT)
    val coinsPagingFlow = coinSort
        .flatMapLatest { collectionRepository.getCoins(sortBy = it, setId = setId) }
        .cachedIn(viewModelScope)

    private var lastCoinsRefresh: Instant? = null

    init {
        observeSet()
        viewModelScope.launch { collectionRepository.storeSet(setId) }
    }

    fun onScreenResumed() {
        viewModelScope.launch { collectionRepository.storeSet(setId) }

        val now = Clock.System.now()
        if (lastCoinsRefresh == null || (now - lastCoinsRefresh!!) >= COINS_REFRESH_THRESHOLD) {
            lastCoinsRefresh = now
            viewModelScope.launch { _events.send(SetScreenEvent.RefreshCoins) }
        }
    }

    private fun observeSet() {
        collectionRepository.getSet(setId)
            .onEach { set -> _state.update { it.copy(set = set) } }
            .launchIn(viewModelScope)
    }

    fun onScreenAction(action: SetScreenAction) {
        when (action) {
            is SetScreenAction.NavigateUp,
            is SetScreenAction.NavigateToCoin -> Unit

            is SetScreenAction.ChangeCoinSort -> changeCoinSort(action.option)

            is SetScreenAction.ToggleCoinMultiSelectMode -> toggleCoinMultiSelect()

            is SetScreenAction.ToggleCoinSelected -> toggleCoinSelected(action.coin)

            is SetScreenAction.RequestRemoveSelectedFromSet ->
                _state.update { it.copy(showRemoveFromSetDialog = true) }

            is SetScreenAction.ConfirmRemoveSelectedFromSet -> removeSelectedFromSet()

            is SetScreenAction.RequestDeleteSelectedCoins ->
                _state.update { it.copy(showDeleteCoinsDialog = true) }

            is SetScreenAction.ConfirmDeleteSelectedCoins -> deleteSelectedCoins()

            is SetScreenAction.DismissDialog ->
                _state.update {
                    it.copy(
                        showRemoveFromSetDialog = false,
                        showDeleteCoinsDialog = false
                    )
                }
        }
    }

    private fun changeCoinSort(option: CoinSortOption) {
        _state.update { it.copy(coinSortOption = option, selectedCoins = emptySet()) }
        coinSort.value = option
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
                selectedCoins = if (coin in current.selectedCoins) {
                    current.selectedCoins - coin
                } else {
                    current.selectedCoins + coin
                }
            )
        }
    }

    private fun removeSelectedFromSet() {
        val ids = _state.value.selectedCoins.map { it.id }
        if (rejectOversizedBulkAction(ids) { it.copy(showRemoveFromSetDialog = false) }) return

        _state.update { it.copy(showRemoveFromSetDialog = false, isProcessingBulkAction = true) }
        viewModelScope.launch {
            handleBulkResult(
                result = collectionRepository.removeCoinsFromSet(setId, ids),
                clearSelection = {
                    it.copy(isCoinMultiSelectModeEnabled = false, selectedCoins = emptySet())
                },
                onSuccess = { SetScreenEvent.CoinsRemovedFromSet(ids.size) },
            )
        }
    }

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
                onSuccess = { SetScreenEvent.CoinsDeleted(it.deleted) },
            )
        }
    }

    private fun rejectOversizedBulkAction(
        ids: List<String>,
        dismissDialog: (SetState) -> SetState,
    ): Boolean {
        if (ids.size <= BULK_ACTION_LIMIT) return false
        _state.update(dismissDialog)
        _events.trySend(SetScreenEvent.BulkActionBlocked("Cannot delete more than $BULK_ACTION_LIMIT items at once."))
        return true
    }

    private suspend fun <T> handleBulkResult(
        result: NetworkResult<T, NetworkError>,
        clearSelection: (SetState) -> SetState,
        onSuccess: (T) -> SetScreenEvent,
    ) {
        when (result) {
            is NetworkResult.Success -> {
                _state.update { clearSelection(it).copy(isProcessingBulkAction = false) }
                collectionRepository.storeSet(setId)
                _events.send(onSuccess(result.data))
            }

            is NetworkResult.Error -> {
                _state.update { it.copy(isProcessingBulkAction = false) }
                _events.send(SetScreenEvent.Error(result.error))
            }
        }
    }

    companion object {
        private const val BULK_ACTION_LIMIT = 200
        private val COINS_REFRESH_THRESHOLD = 30.seconds
    }
}
