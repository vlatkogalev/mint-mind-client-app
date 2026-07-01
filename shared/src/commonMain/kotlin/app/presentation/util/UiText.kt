package app.presentation.util

import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString

sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    class StaticResource(
        val stringResource: StringResource,
        vararg val args: Any,
    ) : UiText()

    class PluralStaticResource(
        val pluralsResource: PluralStringResource,
        val quantity: Int,
        vararg val args: Any,
    ) : UiText()

    suspend fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StaticResource -> getString(stringResource, *args)
            is PluralStaticResource -> getPluralString(pluralsResource, quantity, *args)
        }
    }
}