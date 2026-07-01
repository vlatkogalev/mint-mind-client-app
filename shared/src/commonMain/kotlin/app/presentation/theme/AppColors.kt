package app.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AppColors(
    val gold: Color,
    val silver: Color,
    val copper: Color,
    val textSecondary: Color,
    val matchHigh: Color,
)

val LocalAppColors = staticCompositionLocalOf {
    AppColors(
        gold = Color(0xFFD4A24E),
        silver = Color(0xFFC7CCD3),
        copper = Color(0xFFB87333),
        textSecondary = Color(0xFFA89A82),
        matchHigh = Color(0xFFE8A33D)
    )
}