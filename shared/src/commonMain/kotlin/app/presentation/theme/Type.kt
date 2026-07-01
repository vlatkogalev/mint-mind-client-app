package app.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import mintmind.shared.generated.resources.HubotSans_Bold
import mintmind.shared.generated.resources.HubotSans_BoldItalic
import mintmind.shared.generated.resources.HubotSans_ExtraBold
import mintmind.shared.generated.resources.HubotSans_ExtraBoldItalic
import mintmind.shared.generated.resources.HubotSans_Italic
import mintmind.shared.generated.resources.HubotSans_Light
import mintmind.shared.generated.resources.HubotSans_LightItalic
import mintmind.shared.generated.resources.HubotSans_Medium
import mintmind.shared.generated.resources.HubotSans_MediumItalic
import mintmind.shared.generated.resources.HubotSans_Regular
import mintmind.shared.generated.resources.HubotSans_SemiBold
import mintmind.shared.generated.resources.HubotSans_SemiBoldItalic
import mintmind.shared.generated.resources.MonaSans_Bold
import mintmind.shared.generated.resources.MonaSans_BoldItalic
import mintmind.shared.generated.resources.MonaSans_ExtraBold
import mintmind.shared.generated.resources.MonaSans_ExtraBoldItalic
import mintmind.shared.generated.resources.MonaSans_Italic
import mintmind.shared.generated.resources.MonaSans_Light
import mintmind.shared.generated.resources.MonaSans_LightItalic
import mintmind.shared.generated.resources.MonaSans_Medium
import mintmind.shared.generated.resources.MonaSans_MediumItalic
import mintmind.shared.generated.resources.MonaSans_Regular
import mintmind.shared.generated.resources.MonaSans_SemiBold
import mintmind.shared.generated.resources.MonaSans_SemiBoldItalic
import mintmind.shared.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
private fun monaSans() = FontFamily(
    Font(Res.font.MonaSans_Light, weight = FontWeight.Light),
    Font(Res.font.MonaSans_LightItalic, weight = FontWeight.Light, style = FontStyle.Italic),
    Font(Res.font.MonaSans_Regular, weight = FontWeight.Normal),
    Font(Res.font.MonaSans_Italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(Res.font.MonaSans_Medium, weight = FontWeight.Medium),
    Font(Res.font.MonaSans_MediumItalic, weight = FontWeight.Medium, style = FontStyle.Italic),
    Font(Res.font.MonaSans_SemiBold, weight = FontWeight.SemiBold),
    Font(Res.font.MonaSans_SemiBoldItalic, weight = FontWeight.SemiBold),
    Font(Res.font.MonaSans_Bold, weight = FontWeight.Bold),
    Font(Res.font.MonaSans_BoldItalic, weight = FontWeight.Bold, style = FontStyle.Italic),
    Font(Res.font.MonaSans_ExtraBold, weight = FontWeight.ExtraBold),
    Font(
        Res.font.MonaSans_ExtraBoldItalic,
        weight = FontWeight.ExtraBold,
        style = FontStyle.Italic
    ),
)

@Composable
private fun hubotSans() = FontFamily(
    Font(Res.font.HubotSans_Light, weight = FontWeight.Light),
    Font(Res.font.HubotSans_LightItalic, weight = FontWeight.Light, style = FontStyle.Italic),
    Font(Res.font.HubotSans_Regular, weight = FontWeight.Normal),
    Font(Res.font.HubotSans_Italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(Res.font.HubotSans_Medium, weight = FontWeight.Medium),
    Font(Res.font.HubotSans_MediumItalic, weight = FontWeight.Medium, style = FontStyle.Italic),
    Font(Res.font.HubotSans_SemiBold, weight = FontWeight.SemiBold),
    Font(Res.font.HubotSans_SemiBoldItalic, weight = FontWeight.SemiBold),
    Font(Res.font.HubotSans_Bold, weight = FontWeight.Bold),
    Font(Res.font.HubotSans_BoldItalic, weight = FontWeight.Bold, style = FontStyle.Italic),
    Font(Res.font.HubotSans_ExtraBold, weight = FontWeight.ExtraBold),
    Font(
        Res.font.HubotSans_ExtraBoldItalic,
        weight = FontWeight.ExtraBold,
        style = FontStyle.Italic
    ),
)

val baseline = Typography()

@Composable
fun appTypography() = Typography().run {
    val displayFontFamily = hubotSans()
    val bodyFontFamily = monaSans()

    copy(
        displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
    )
}

