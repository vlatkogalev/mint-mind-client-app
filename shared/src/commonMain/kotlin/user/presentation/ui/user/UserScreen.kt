@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package user.presentation.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowRight
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.presentation.components.AppTopBar
import app.presentation.components.dialog.ConfirmDialog
import app.presentation.components.dialog.RedirectDialog
import app.presentation.theme.AppTheme
import app.presentation.util.calculateGridConfig
import app.presentation.util.cutoutAwarePadding
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.cancel
import mintmind.shared.generated.resources.user_logout
import mintmind.shared.generated.resources.user_logout_message
import org.jetbrains.compose.resources.stringResource
import user.presentation.components.LanguageDialog
import user.presentation.components.ThemeDialog
import user.presentation.model.SettingsGroup

@Composable
fun UserScreen(
    state: UserState,
    snackbarHostState: SnackbarHostState,
    onScreenAction: (action: UserScreenAction) -> Unit,
) {
    Scaffold(
        topBar = {
            AppTopBar(
                onNavigateUp = { onScreenAction(UserScreenAction.NavigateUp) },
                transparent = true,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        MainContent(
            state = state,
            paddingValues = paddingValues,
            onScreenAction = onScreenAction,
        )
    }

    if (state.showThemeDialog) {
        ThemeDialog(
            initialTheme = state.theme,
            onConfirmed = { onScreenAction(UserScreenAction.ChangeTheme(it)) },
            onDismissRequest = { onScreenAction(UserScreenAction.ToggleThemeDialog) }
        )
    }

    if (state.showLanguageDialog) {
        LanguageDialog(
            initialLanguage = state.language,
            onConfirmed = { onScreenAction(UserScreenAction.ChangeLanguage(it)) },
            onDismissRequest = { onScreenAction(UserScreenAction.ToggleLanguageDialog) }
        )
    }

    if (state.showRedirectDialog) {
        state.redirectUri?.let { uri ->
            RedirectDialog(
                uri = uri,
                onDismissRequest = {
                    onScreenAction(UserScreenAction.ToggleRedirectDialog(null))
                }
            )
        }
    }

    if (state.showLogoutDialog) {
        ConfirmDialog(
            title = stringResource(Res.string.user_logout),
            text = stringResource(Res.string.user_logout_message),
            positiveButtonText = stringResource(Res.string.user_logout),
            negativeButtonText = stringResource(Res.string.cancel),
            imageVector = Icons.AutoMirrored.Outlined.Logout,
            onConfirmAction = { onScreenAction(UserScreenAction.ConfirmLogout) },
            onDismissRequest = { onScreenAction(UserScreenAction.DismissLogoutDialog) }
        )
    }
}

@Composable
private fun MainContent(
    state: UserState,
    paddingValues: PaddingValues,
    onScreenAction: (action: UserScreenAction) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val gridConfig = windowSizeClass.calculateGridConfig(1, 2, 3)

    val groups = remember(state, onScreenAction) {
        SettingsGroup.create(state, onScreenAction)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .cutoutAwarePadding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            )
    ) {
        LazyVerticalGrid(
            columns = gridConfig.gridCells,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(groups) { group ->
                SettingsGroupSection(
                    group = group,
                    isInteractionDisabled = state.isLoading
                )
            }
        }

        if (state.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun SettingsGroupSection(
    group: SettingsGroup,
    isInteractionDisabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        group.items.forEachIndexed { index, item ->
            SegmentedListItem(
                onClick = item.onClick,
                enabled = !isInteractionDisabled,
                shapes = ListItemDefaults.segmentedShapes(
                    index = index,
                    count = group.items.size
                ),
                leadingContent = {
                    Icon(item.leadingIcon, contentDescription = null)
                },
                trailingContent = {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowRight,
                        contentDescription = null
                    )
                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    leadingIconColor = MaterialTheme.colorScheme.primary,
                    trailingIconColor = MaterialTheme.colorScheme.primary,
                ),
                contentPadding = ListItemDefaults.ContentPadding,
                supportingContent = item.supportingText?.let { text ->
                    {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        )
                    }
                }
            ) {
                Text(
                    text = stringResource(item.title),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview
@Composable
private fun UserScreenPreview() {
    AppTheme {
        UserScreen(
            state = UserState(),
            snackbarHostState = remember { SnackbarHostState() },
            onScreenAction = {}
        )
    }
}
