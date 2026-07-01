package notification

interface PushNotificationService {
    fun onApplicationStartPlatformSpecific()
    suspend fun subscribeToTopic(topic: String)
    suspend fun unsubscribeFromTopic(topic: String)
}

expect fun getPushNotificationService(): PushNotificationService
