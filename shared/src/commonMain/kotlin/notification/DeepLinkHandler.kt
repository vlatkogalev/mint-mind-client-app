package notification

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import notification.DeepLinkHandler.consume
import notification.DeepLinkHandler.pending

/**
 * Shared deep link handler for notification click-through navigation.
 *
 * Both Android and iOS write a [DeepLink] when the user taps a push notification
 * that contains auction data. The Compose navigation layer observes [pending] and
 * navigates accordingly, then calls [consume] to clear the pending deep link.
 */
object DeepLinkHandler {
    private val _pending = MutableStateFlow<DeepLink?>(null)
    val pending: StateFlow<DeepLink?> = _pending.asStateFlow()

    fun offer(deepLink: DeepLink) {
        _pending.value = deepLink
    }

    fun consume() {
        _pending.value = null
    }
}

sealed interface DeepLink {
    data class Auction(val slug: String, val auctionId: Long) : DeepLink
}
