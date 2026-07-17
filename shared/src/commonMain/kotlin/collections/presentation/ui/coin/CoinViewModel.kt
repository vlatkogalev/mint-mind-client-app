package collections.presentation.ui.coin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.domain.model.NetworkResult
import collections.domain.CollectionRepository
import collections.domain.model.CoinSetSortOption
import collections.presentation.mapper.CoinUiMapper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoinViewModel(
    private val coinId: String,
    private val collectionRepository: CollectionRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(CoinState())
    val state: StateFlow<CoinState> = _state.asStateFlow()

    private val _events = Channel<CoinScreenEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        storeCoin()
        fetchCoinDetails()
        observeSets()
        refreshSets()
    }

    fun onScreenAction(action: CoinScreenAction) {
        when (action) {
            is CoinScreenAction.NavigateUp -> Unit
            is CoinScreenAction.ShowMoveSheet ->
                _state.update { it.copy(showMoveSheet = true) }

            is CoinScreenAction.DismissMoveSheet ->
                _state.update { it.copy(showMoveSheet = false) }

            is CoinScreenAction.MoveToSet -> moveCoinToSet(action.targetSetId)
        }
    }

    private fun storeCoin() = viewModelScope.launch {
        collectionRepository.storeCoinDetails(coinId)
    }

    private fun fetchCoinDetails() = viewModelScope.launch {
        collectionRepository.getCoinDetails(coinId).collect { coin ->
            _state.value = _state.value.copy(
                coin = coin?.let { CoinUiMapper.map(it) }
            )
        }
    }

    private fun observeSets() {
        collectionRepository.getSets(CoinSetSortOption.DEFAULT)
            .onEach { sets -> _state.update { it.copy(sets = sets) } }
            .launchIn(viewModelScope)
    }

    private fun refreshSets() {
        viewModelScope.launch { collectionRepository.storeSets() }
    }

    private fun moveCoinToSet(targetSetId: String) {
        val setName = _state.value.sets.firstOrNull { it.id == targetSetId }?.name.orEmpty()
        _state.update { it.copy(showMoveSheet = false, isProcessingMove = true) }
        viewModelScope.launch {
            when (val result = collectionRepository.moveCoinsToSet(targetSetId, listOf(coinId))) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(isProcessingMove = false) }
                    _events.send(CoinScreenEvent.CoinMoved(1, setName))
                }

                is NetworkResult.Error -> {
                    _state.update { it.copy(isProcessingMove = false) }
                    _events.send(CoinScreenEvent.Error(result.error))
                }
            }
        }
    }
}
