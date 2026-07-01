package feed.domain.util

val currencySymbolMap: Map<String, String> = mapOf(
    // Major World Currencies
    "USD" to "$",      // United States Dollar
    "EUR" to "€",      // Euro
    "JPY" to "¥",      // Japanese Yen
    "GBP" to "£",      // British Pound Sterling
    "AUD" to "A$",     // Australian Dollar
    "CAD" to "CA$",    // Canadian Dollar
    "CHF" to "CHF",    // Swiss Franc
    "CNY" to "¥",      // Chinese Yuan
    "HKD" to "HK$",    // Hong Kong Dollar
    "NZD" to "NZ$",    // New Zealand Dollar

    // Americas
    "BRL" to "R$",     // Brazilian Real
    "MXN" to "MX$",    // Mexican Peso
    "ARS" to "$",      // Argentine Peso
    "CLP" to "$",      // Chilean Peso
    "COP" to "$",      // Colombian Peso
    "PEN" to "S/",     // Peruvian Sol

    // Europe (Non-Euro)
    "SEK" to "kr",     // Swedish Krona
    "NOK" to "kr",     // Norwegian Krone
    "DKK" to "kr",     // Danish Krone
    "PLN" to "zł",     // Polish Złoty
    "HUF" to "Ft",     // Hungarian Forint
    "CZK" to "Kč",     // Czech Koruna
    "RUB" to "₽",      // Russian Ruble
    "TRY" to "₺",      // Turkish Lira
    "UAH" to "₴",      // Ukrainian Hryvnia

    // Asia
    "INR" to "₹",      // Indian Rupee
    "KRW" to "₩",      // South Korean Won
    "SGD" to "S$",     // Singapore Dollar
    "THB" to "฿",      // Thai Baht
    "IDR" to "Rp",     // Indonesian Rupiah
    "PHP" to "₱",      // Philippine Peso
    "MYR" to "RM",     // Malaysian Ringgit
    "VND" to "₫",      // Vietnamese Đồng
    "TWD" to "NT$",    // New Taiwan Dollar
    "PKR" to "₨",      // Pakistani Rupee
    "BDT" to "৳",      // Bangladeshi Taka

    // Middle East & Africa
    "ILS" to "₪",      // Israeli New Shekel
    "AED" to "د.إ",   // United Arab Emirates Dirham
    "SAR" to "﷼",      // Saudi Riyal
    "ZAR" to "R",      // South African Rand
    "EGP" to "E£",     // Egyptian Pound
    "NGN" to "₦",      // Nigerian Naira
    "KES" to "KSh"     // Kenyan Shilling
)

/**
 * Gets a currency symbol from the manually defined map.
 *
 * @param currencyCode The 3-letter ISO 4217 currency code (e.g., "USD").
 * @return The corresponding currency symbol (e.g., "$") or the original code if not found.
 */
fun getCurrencySymbol(currencyCode: String): String {
    return currencySymbolMap[currencyCode.uppercase()] ?: currencyCode
}