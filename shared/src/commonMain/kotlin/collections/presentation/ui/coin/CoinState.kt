package collections.presentation.ui.coin

import collections.presentation.model.CoinUiModel

data class CoinState(
    val isLoading: Boolean = false,
    val coin: CoinUiModel? = null,
)
