package app.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import app.presentation.theme.AppTheme
import app.presentation.theme.AppThemeExt
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    transparent: Boolean = false,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val containerColor = if (transparent) Color.Transparent
    else MaterialTheme.colorScheme.background

    CenterAlignedTopAppBar(
        title = title,
        navigationIcon = { navigationIcon?.invoke() },
        actions = actions,
        colors = TopAppBarColors(
            containerColor = containerColor,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.tertiary,
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.tertiary,
            subtitleContentColor = AppThemeExt.colors.textSecondary,
        ),
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: @Composable () -> Unit = {},
    onNavigateUp: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    transparent: Boolean = false,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    AppTopBar(
        title = title,
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
            }
        },
        actions = actions,
        transparent = transparent,
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AppTopBarHomePreview() {
    AppTheme {
        AppTopBar(
            title = { TopAppBarText(text = stringResource(Res.string.app_name)) },
            actions = {
                IconButton(onClick = { }) {
                    Icon(Icons.Outlined.AccountCircle, contentDescription = null)
                }
            },
            transparent = true,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AppTopBarDetailPreview() {
    AppTheme {
        AppTopBar(
            title = { TopAppBarText(text = "Detail") },
            onNavigateUp = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AppTopBarTransparentNavPreview() {
    AppTheme {
        AppTopBar(
            onNavigateUp = {},
            transparent = true,
        )
    }
}
