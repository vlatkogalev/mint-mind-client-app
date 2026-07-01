package app.util

import android.text.format.DateUtils
import io.github.aakira.napier.Napier
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.time.ExperimentalTime
import kotlin.time.toKotlinInstant

actual fun Long?.toCustomFormat(format: String, zone: String): String {
    return this?.let { epochInMilli ->
        val instant = Instant.ofEpochMilli(epochInMilli)
        val zonedDateTime = instant.atZone(ZoneId.of(zone))
        val formatter = DateTimeFormatter.ofPattern(format, Locale.getDefault())
        zonedDateTime.format(formatter)
    } ?: run {
        ""
    }
}

actual fun Long.epochMilliToDateRange(): String {
    return try {
        DateUtils.getRelativeTimeSpanString(
            this,
            Instant.now().toEpochMilli(),
            0,
            DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString()
    } catch (e: Exception) {
        Napier.e("toDateRange: ${e.localizedMessage}")
        ""
    }
}

actual fun Long.toLocalDateTime(shortTime: Boolean): String {
    val instant = Instant.ofEpochMilli(this)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    val localePattern = DateTimeFormatter.ofLocalizedDateTime(
        FormatStyle.MEDIUM,
        if (shortTime) FormatStyle.SHORT else FormatStyle.MEDIUM
    )

    return localDateTime.format(localePattern)
}

@OptIn(markerClass = [ExperimentalTime::class])
internal actual fun stringToInstant(
    dateString: String,
    formatPattern: String
): kotlin.time.Instant? {
    return try {
        val formatter = DateTimeFormatter.ofPattern(formatPattern, Locale.US)
        ZonedDateTime.parse(dateString, formatter).toInstant().toKotlinInstant()
    } catch (e: DateTimeParseException) {
        println("Failed to parse date string: '$dateString' with pattern '$formatPattern'. Error: ${e.message}")
        null
    } catch (e: IllegalArgumentException) {
        println("Invalid format pattern: '$formatPattern'. Error: ${e.message}")
        null
    }
}