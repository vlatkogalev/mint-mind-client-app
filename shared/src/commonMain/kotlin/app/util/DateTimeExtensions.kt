package app.util

import app.domain.DateFormat
import io.github.aakira.napier.Napier
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetIn
import kotlinx.datetime.toInstant
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

val DEFAULT_TIMEZONE: TimeZone = TimeZone.of("Europe/Zurich")

expect fun Long?.toCustomFormat(format: String, zone: String = "UTC"): String

/**
 * Converts a timestamp in milliseconds to a human-readable relative time span string.
 *
 * @return A String representing a human-readable relative time span between the given timestamp and
 *         the current time. The format is abbreviated and may include phrases like "in x minutes"
 *         or "x minutes ago."
 *         If an exception occurs during the conversion, an empty string is returned.
 */
expect fun Long.epochMilliToDateRange(): String

/**
 * Checks if this instant is before the specified instant.
 *
 * @param other the other instant to compare to, not null
 * @return true if this instant is before the specified instant
 */
@OptIn(ExperimentalTime::class)
fun Instant.isBefore(other: Instant): Boolean = this < other

/**
 * Checks if this instant is after the specified instant.
 *
 * @param other the other instant to compare to, not null
 * @return true if this instant is after the specified instant
 */
@OptIn(ExperimentalTime::class)
fun Instant.isAfter(other: Instant): Boolean = this > other

/**
 * Parses a nullable ISO-8601 formatted date string into a Unix timestamp (epoch milliseconds).
 *
 * This function utilizes `kotlinx.datetime.Instant.parse()` to handle standard ISO-8601 formats,
 * including those with "Z" (Zulu/UTC) or explicit timezone offsets (e.g., "+02:00").
 *
 * Supported formats include:
 * - `2023-11-25T14:30:00Z` (UTC)
 * - `2025-08-11T15:00:00+02:00` (With Offset)
 * - `2023-11-25T14:30:00.123Z` (With Milliseconds)
 *
 * @receiver The date string to parse. Can be null or blank.
 * @return The number of milliseconds that have elapsed since the Unix epoch (1970-01-01T00:00:00Z),
 *         or `null` if the input string is null, blank, or does not strictly follow ISO-8601 format.
 *
 * @see Instant.parse
 */
@OptIn(ExperimentalTime::class)
fun String?.toEpochMillis(): Long? {
    if (this.isNullOrBlank()) return null
    return try {
        Instant.parse(this).toEpochMilliseconds()
    } catch (e: Exception) {
        Napier.e { e.message.orEmpty() }
        null
    }
}

/**
 * Convert milliseconds since the epoch to a formatted LocalDateTime string using the system's
 * default time zone and a customizable format.
 *
 * @receiver The Long value representing milliseconds since the epoch.
 * @param shortTime Whether to include a short time format. Default is true.
 * @return The formatted LocalDateTime string with a medium date format and an optional
 *         short or medium time format.
 */
expect fun Long.toLocalDateTime(shortTime: Boolean = true): String

/**
 * Parses a display-formatted date string (dd/MM/yyyy) into a [LocalDate].
 *
 * @receiver The date string in dd/MM/yyyy format (e.g. "04/03/2026").
 * @return The corresponding [LocalDate], or `null` if the string is malformed.
 */
fun String.toLocalDateOrNull(): LocalDate? {
    val parts = split("/")
    if (parts.size != 3) return null
    return try {
        val day = parts[0].toInt()
        val month = parts[1].toInt()
        val year = parts[2].toInt()
        LocalDate(year, month, day)
    } catch (_: Exception) {
        null
    }
}

/**
 * Converts a display-formatted date string (dd/MM/yyyy) to an ISO 8601 **date** string.
 *
 * Example: `"04/03/2026"` → `"2026-03-04"`
 *
 * @receiver The date string in dd/MM/yyyy format.
 * @return The ISO 8601 date string, or `null` if the input is malformed.
 */
fun String.toIsoDateString(): String? = toLocalDateOrNull()?.toString()

/**
 * Converts a display-formatted date string (dd/MM/yyyy) to an ISO 8601 **date-time** string
 * with time set to midnight, including the UTC offset for the given [timeZone].
 *
 * Example (CET): `"04/03/2026"` → `"2026-03-04T00:00:00+01:00"`
 * Example (CEST): `"04/07/2026"` → `"2026-07-04T00:00:00+02:00"`
 *
 * @receiver The date string in dd/MM/yyyy format.
 * @param timeZone The timezone to calculate the offset for. Defaults to [DEFAULT_TIMEZONE].
 * @return The ISO 8601 date-time string with offset, or `null` if the input is malformed.
 */
fun String.toIsoDateTimeString(timeZone: TimeZone = DEFAULT_TIMEZONE): String? {
    val date = toLocalDateOrNull() ?: return null
    val dateTime = LocalDateTime(date, LocalTime(0, 0))
    val instant = dateTime.toInstant(timeZone)
    val offset = instant.offsetIn(timeZone)
    return "${date}T00:00:00${offset}"
}

/**
 * Combines a display-formatted date string (dd/MM/yyyy) with a time string (HH:mm)
 * into an ISO 8601 **date-time** string, including the UTC offset for the given [timeZone].
 *
 * Example (CET): `"04/03/2026".toIsoDateTimeString("14:30")` → `"2026-03-04T14:30:00+01:00"`
 * Example (CEST): `"04/07/2026".toIsoDateTimeString("14:30")` → `"2026-07-04T14:30:00+02:00"`
 *
 * @receiver The date string in dd/MM/yyyy format.
 * @param time The time string in HH:mm format.
 * @param timeZone The timezone to calculate the offset for. Defaults to [DEFAULT_TIMEZONE].
 * @return The ISO 8601 date-time string with offset, or `null` if date or time is malformed.
 */
fun String.toIsoDateTimeString(time: String, timeZone: TimeZone = DEFAULT_TIMEZONE): String? {
    val date = toLocalDateOrNull() ?: return null
    if (!time.matches(Regex("\\d{2}:\\d{2}"))) return null
    val (hour, minute) = time.split(":").map { it.toInt() }
    val dateTime = LocalDateTime(date, LocalTime(hour, minute))
    val instant = dateTime.toInstant(timeZone)
    val offset = instant.offsetIn(timeZone)
    return "${date}T${time}:00${offset}"
}

/**
 * Expects a platform-specific implementation to parse a date string
 * using a given format pattern. Returns null if parsing fails.
 */
@OptIn(ExperimentalTime::class)
internal expect fun stringToInstant(dateString: String, formatPattern: String): Instant?

/**
 * Extension function to convert a date string of a given format
 * into a Unix timestamp (seconds since the epoch).
 *
 * @param format The [DateFormat] describing the layout of the date string.
 * @return The Unix timestamp as a Long, or null if the string is invalid.
 */
@OptIn(ExperimentalTime::class)
fun String.toUnixTimestamp(format: DateFormat): Long? {
    return stringToInstant(this, format.pattern)?.epochSeconds?.times(1000)
}