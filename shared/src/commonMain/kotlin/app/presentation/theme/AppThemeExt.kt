package app.presentation.theme

import androidx.compose.runtime.Composable

object AppThemeExt {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current

    val statusColors: StatusColors
        @Composable
        get() = LocalStatusColors.current
}