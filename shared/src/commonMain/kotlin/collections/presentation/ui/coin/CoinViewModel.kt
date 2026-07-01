package collections.presentation.ui.coin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import collections.presentation.mapper.CoinUiMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CoinViewModel(
    private val coinId: String,
    private val collectionRepository: collections.domain.CollectionRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(CoinState())
    val state: StateFlow<CoinState> = _state

    init {
        storeCoin()
        fetchCoinDetails()
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
}
