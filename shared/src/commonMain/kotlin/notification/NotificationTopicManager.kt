package notification

import app.domain.model.onError
import app.domain.model.onSuccess
import io.github.aakira.napier.Napier
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import notification.data.local.NotificationTopicStore
import notification.domain.NotificationTopicsRepository
import notification.domain.model.NotificationTopicSyncReason
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class NotificationTopicManager(
    private val notificationTopicStore: NotificationTopicStore,
    private val notificationTopicsRepository: NotificationTopicsRepository,
    private val pushNotificationService: PushNotificationService,
    private val isAuthenticated: suspend () -> Boolean,
    private val clock: Clock = Clock.System,
    private val foregroundCooldown: Duration = 30.seconds,
    private val interactionCooldown: Duration = 5.seconds,
) {
    private val mutex = Mutex()
    private var lastForegroundSyncAt: Instant? = null
    private var lastInteractionSyncAt: Instant? = null

    suspend fun syncNow(reason: NotificationTopicSyncReason): Boolean = mutex.withLock {
        if (reason == NotificationTopicSyncReason.Foreground && !isCooldownElapsed(
                lastForegroundSyncAt,
                foregroundCooldown
            )
        ) {
            Napier.d(tag = TAG) { "Skipping foreground topic sync due to cooldown." }
            return true
        }

        if (reason == NotificationTopicSyncReason.AuctionInteraction && !isCooldownElapsed(
                lastInteractionSyncAt,
                interactionCooldown
            )
        ) {
            Napier.d(tag = TAG) { "Skipping auction interaction topic sync due to cooldown." }
            return true
        }

        if (!isAuthenticated()) {
            Napier.d(tag = TAG) { "Skipping topic sync because user is not authenticated." }
            return false
        }

        var didSucceed = false

        notificationTopicsRepository.getAssignedTopics()
            .onSuccess { assignedTopics ->
                val desiredTopics = assignedTopics.toSet()
                val currentTopics = notificationTopicStore.getSubscribedTopicsOnce()
                val diff = computeDiff(currentTopics, desiredTopics)

                val failedSubscriptions = mutableSetOf<String>()
                val failedUnsubscriptions = mutableSetOf<String>()
                val reconciledTopics = currentTopics.toMutableSet()

                diff.toSubscribe.forEach { topic ->
                    runCatching {
                        pushNotificationService.subscribeToTopic(topic)
                    }.onSuccess {
                        reconciledTopics += topic
                    }.onFailure {
                        Napier.w(tag = TAG) { "Failed to subscribe to topic: $topic" }
                        failedSubscriptions += topic
                    }
                }
                diff.toUnsubscribe.forEach { topic ->
                    runCatching {
                        pushNotificationService.unsubscribeFromTopic(topic)
                    }.onSuccess {
                        reconciledTopics -= topic
                    }.onFailure {
                        Napier.w(tag = TAG) { "Failed to unsubscribe from topic: $topic" }
                        failedUnsubscriptions += topic
                    }
                }

                notificationTopicStore.setSubscribedTopics(reconciledTopics)

                val isFullySynced =
                    failedSubscriptions.isEmpty() && failedUnsubscriptions.isEmpty() &&
                            reconciledTopics == desiredTopics

                if (isFullySynced) {
                    when (reason) {
                        NotificationTopicSyncReason.Foreground -> lastForegroundSyncAt = clock.now()
                        NotificationTopicSyncReason.AuctionInteraction -> lastInteractionSyncAt =
                            clock.now()

                        else -> Unit
                    }
                }

                Napier.i(tag = TAG) {
                    buildString {
                        append("Topic sync completed ($reason): ")
                        append("subscribe_attempted=${diff.toSubscribe.size}, unsubscribe_attempted=${diff.toUnsubscribe.size}, total_desired=${desiredTopics.size}")
                        append("\n- failed_subscribe=${failedSubscriptions.size}, failed_unsubscribe=${failedUnsubscriptions.size}")
                        if (failedSubscriptions.isNotEmpty()) append("\n- failed subscribe topics: ${failedSubscriptions.joinToString()}")
                        if (failedUnsubscriptions.isNotEmpty()) append("\n- failed unsubscribe topics: ${failedUnsubscriptions.joinToString()}")
                        append("\n- reconciled topics: ${reconciledTopics.joinToString()}")
                    }
                }
                didSucceed = isFullySynced
            }
            .onError { error ->
                Napier.w(tag = TAG) {
                    "Topic sync failed ($reason): $error. Keeping local snapshot unchanged for next retry."
                }
            }

        didSucceed
    }

    suspend fun unsubscribeAllLocalAndClear(): Boolean = mutex.withLock {
        val localTopics = notificationTopicStore.getSubscribedTopicsOnce()
        val failedTopics = mutableSetOf<String>()

        localTopics.forEach { topic ->
            runCatching {
                pushNotificationService.unsubscribeFromTopic(topic)
            }.onFailure {
                Napier.w(tag = TAG) { "Failed to unsubscribe from topic: $topic" }
                failedTopics += topic
            }
        }

        if (failedTopics.isEmpty()) {
            notificationTopicStore.clearSubscribedTopics()
        } else {
            notificationTopicStore.setSubscribedTopics(failedTopics)
        }

        val unsubscribed = localTopics.size - failedTopics.size
        Napier.i(tag = TAG) {
            "Cleared topic subscriptions on logout: unsubscribed=$unsubscribed, failed=${failedTopics.size}"
        }

        failedTopics.isEmpty()
    }

    private fun isCooldownElapsed(lastSyncAt: Instant?, cooldown: Duration): Boolean {
        val last = lastSyncAt ?: return true
        return clock.now() - last >= cooldown
    }

    internal fun computeDiff(currentTopics: Set<String>, desiredTopics: Set<String>): TopicDiff {
        val toSubscribe = desiredTopics - currentTopics
        val toUnsubscribe = currentTopics - desiredTopics

        return TopicDiff(
            toSubscribe = toSubscribe,
            toUnsubscribe = toUnsubscribe,
        )
    }

    internal data class TopicDiff(
        val toSubscribe: Set<String>,
        val toUnsubscribe: Set<String>,
    )

    private companion object {
        private const val TAG = "NotificationTopicSync"
    }
}
