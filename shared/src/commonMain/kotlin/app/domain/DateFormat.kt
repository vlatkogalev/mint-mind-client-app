package app.domain

/**
 * A sealed class representing date format patterns for parsing.
 * Provides common formats and allows for custom patterns.
 */
sealed class DateFormat(val pattern: String) {
    /**
     * RFC 2822 / RFC 1123 format.
     * Example: "Wed, 17 Sep 2025 01:02:39 +0000"
     */
    data object RFC_2822 : DateFormat("EEE, dd MMM yyyy HH:mm:ss Z")

    /**
     * ISO 8601 format with timezone offset.
     * Example: "2025-09-17T01:02:39+00:00"
     */
    data object ISO_8601 : DateFormat("yyyy-MM-dd'T'HH:mm:ssZ")

    /**
     * ISO 8601 format with milliseconds and a Zulu (Z) timezone.
     * The `.SSS` handles the fraction of a second, and `X` handles the 'Z'.
     * Example: "2025-10-07T16:05:00.000Z"
     */
    data object ISO_8601_WITH_MILLIS : DateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")

    /**
     * A simple date and time format.
     * Example: "2025-09-17 01:02:39"
     */
    data object SimpleDateTime : DateFormat("yyyy-MM-dd HH:mm:ss")

    /**
     * For any other custom format pattern.
     * @param pattern The date format string (e.g., "dd/MM/yyyy").
     */
    class Custom(pattern: String) : DateFormat(pattern)
}