package feed.presentation.ui.coin_listings

interface CoinListingsScreenAction {
    data object NavigateUp : CoinListingsScreenAction
    data class ToggleRedirectDialog(val redirectUri: String?) : CoinListingsScreenAction
    data class LoadCoinListing(val url: String?) : CoinListingsScreenAction
}