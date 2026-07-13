package collections.presentation.ui.set

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import app.presentation.components.AppTopBar
import app.presentation.theme.AppTheme

@Composable
fun SetScreen(
    state: SetState,
    snackbarHostState: SnackbarHostState,
    onScreenAction: (SetScreenAction) -> Unit,
) {
    Scaffold(
        topBar = {
            AppTopBar(
                onNavigateUp = { onScreenAction(SetScreenAction.NavigateUp) },
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
}

@Composable
private fun MainContent(
    state: SetState,
    paddingValues: PaddingValues,
    onScreenAction: (action: SetScreenAction) -> Unit,
) {

}

@Preview
@Composable
private fun SetScreenPreview() {
    AppTheme {
        SetScreen(
            state = SetState(),
            snackbarHostState = remember { SnackbarHostState() },
            onScreenAction = {}
        )
    }
}