package app.util

import kotlinx.cinterop.BetaInteropApi
import kotlinx.datetime.toKotlinInstant
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterMediumStyle
import platform.Foundation.NSDateFormatterShortStyle
import platform.Foundation.NSLocale
import platform.Foundation.NSRelativeDateTimeFormatter
import platform.Foundation.NSTimeZone
import platform.Foundation.autoupdatingCurrentLocale
import platform.Foundation.create
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.localTimeZone
import platform.Foundation.now
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(BetaInteropApi::class)
actual fun Long?.toCustomFormat(format: String, zone: String): String {
    return this?.let { epochInMillis ->
        val date = NSDate.dateWithTimeIntervalSince1970(epochInMillis.div(1000).toDouble())
        val dateFormatter = NSDateFormatter().apply {
            timeZone = NSTimeZone.create(zone) ?: NSTimeZone.localTimeZone
            locale = NSLocale.autoupdatingCurrentLocale
            dateFormat = format
        }
        dateFormatter.stringFromDate(date)
    } ?: run {
        ""
    }
}

@OptIn(BetaInteropApi::class)
actual fun Long.epochMilliToDateRange(): String {
    val date = NSDate.dateWithTimeIntervalSince1970(this.div(1000).toDouble())
    val currentDate = NSDate.now
    val dateFormatter = NSRelativeDateTimeFormatter()
    return dateFormatter.localizedStringForDate(date, currentDate)
}

actual fun Long.toLocalDateTime(shortTime: Boolean): String {
    val date = NSDate.dateWithTimeIntervalSince1970(this / 1000.0)
    val formatter = NSDateFormatter()

    formatter.dateStyle = NSDateFormatterMediumStyle
    formatter.timeStyle = if (shortTime) NSDateFormatterShortStyle else NSDateFormatterMediumStyle

    return formatter.stringFromDate(date)
}

@OptIn(markerClass = [ExperimentalTime::class])
internal actual fun stringToInstant(
    dateString: String,
    formatPattern: String
): Instant? {
    val formatter = NSDateFormatter()
    formatter.dateFormat = formatPattern
    // Use a POSIX locale to ensure parsing works consistently across devices.
    formatter.locale = NSLocale("en_US_POSIX")

    val nsDate = formatter.dateFromString(dateString)
    if (nsDate == null) {
        println("Failed to parse date string: '$dateString' with pattern '$formatPattern'.")
    }
    return nsDate?.toKotlinInstant()
}