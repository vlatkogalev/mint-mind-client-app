package feed.presentation.ui.coin_listings

data class CoinListingsState(
    val showRedirectDialog: Boolean = false,
    val redirectUri: String? = null,
)
