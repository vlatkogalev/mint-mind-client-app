package app.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.time.Clock

class CountdownTimer(
    private val scope: CoroutineScope,
    private val intervalMillis: Long = 1000L
) {
    private var timerJob: Job? = null

    /**
     * Starts a countdown timer from the given timestamp
     * @param targetTimestamp Target time in milliseconds since epoch
     * @param onTick Callback invoked every [intervalMillis] with remaining milliseconds
     * @param onFinish Callback invoked when countdown finishes
     */
    fun start(
        targetTimestamp: Long,
        onTick: (millisRemaining: Long) -> Unit,
        onFinish: () -> Unit
    ) {
        cancel()

        timerJob = scope.launch {
            val millisInFuture = targetTimestamp - Clock.System.now().toEpochMilliseconds()

            if (millisInFuture <= 0) {
                onFinish()
                return@launch
            }

            var remaining = millisInFuture

            while (remaining > 0) {
                onTick(remaining)
                delay(intervalMillis)
                remaining = targetTimestamp - Clock.System.now().toEpochMilliseconds()
            }

            onFinish()
        }
    }

    /**
     * Returns a Flow that emits remaining milliseconds at each interval
     * @param targetTimestamp Target time in milliseconds since epoch
     */
    fun startAsFlow(targetTimestamp: Long): Flow<Long> = flow {
        val millisInFuture = targetTimestamp - Clock.System.now().toEpochMilliseconds()

        if (millisInFuture <= 0) {
            emit(0L)
            return@flow
        }

        var remaining = millisInFuture

        while (remaining > 0) {
            emit(remaining)
            delay(intervalMillis)
            remaining = targetTimestamp - Clock.System.now().toEpochMilliseconds()
        }

        emit(0L)
    }

    /**
     * Cancels the running timer
     */
    fun cancel() {
        timerJob?.cancel()
        timerJob = null
    }

    /**
     * Checks if timer is currently running
     */
    fun isRunning(): Boolean = timerJob?.isActive == true
}