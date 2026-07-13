package auth.presentation.ui.reset_password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.presentation.components.AppTopBar
import app.presentation.components.ConfirmDialog
import app.presentation.components.FormTextField
import app.presentation.components.PrimaryButton
import app.presentation.theme.AppTheme
import app.presentation.util.calculateAdaptiveWidth
import app.presentation.util.cutoutAwarePadding
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.forgot_password_button_change_password
import mintmind.shared.generated.resources.forgot_password_request_successful
import mintmind.shared.generated.resources.forgot_password_screen_title
import mintmind.shared.generated.resources.forgot_password_text
import mintmind.shared.generated.resources.forgot_password_title
import mintmind.shared.generated.resources.login_email_hint
import mintmind.shared.generated.resources.ok
import org.jetbrains.compose.resources.stringResource

@Composable
fun ResetPasswordScreen(
    state: ResetPasswordState,
    snackbarHostState: SnackbarHostState,
    onFormEvent: (event: ResetPasswordScreenEvent) -> Unit,
    onScreenAction: (action: ResetPasswordScreenAction) -> Unit,
) {
    val scrollState = rememberScrollState()
    val formWidth = currentWindowAdaptiveInfoV2().windowSizeClass.calculateAdaptiveWidth()

    Scaffold(
        topBar = {
            AppTopBar(
                onNavigateUp = { onScreenAction(ResetPasswordScreenAction.NavigateUp) },
                transparent = true,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .cutoutAwarePadding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = 112.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(formWidth),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth().height(112.dp)
                )

                Text(
                    text = stringResource(Res.string.forgot_password_title),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = stringResource(Res.string.forgot_password_text),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                FormTextField(
                    hint = stringResource(Res.string.login_email_hint),
                    value = state.email,
                    error = state.emailError?.let { stringResource(it) },
                    leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                    keyboardType = KeyboardType.Email,
                    onFormEvent = { onFormEvent(ResetPasswordScreenEvent.EmailChanged(it)) },
                    modifier = Modifier.fillMaxWidth()
                )

                PrimaryButton(
                    isLoading = state.isLoading,
                    text = stringResource(Res.string.forgot_password_button_change_password),
                    onButtonClick = { onFormEvent(ResetPasswordScreenEvent.Submit) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (state.showConfirmDialog) {
            ConfirmDialog(
                title = stringResource(Res.string.forgot_password_screen_title),
                text = stringResource(Res.string.forgot_password_request_successful),
                buttonText = stringResource(Res.string.ok),
                imageVector = Icons.Outlined.Lock,
                onDismissAction = {
                    onScreenAction(ResetPasswordScreenAction.ToggleConfirmDialog)
                    onScreenAction(ResetPasswordScreenAction.NavigateUp)
                },
            )
        }
    }
}

@Preview
@Composable
private fun UserResetPasswordScreenPreview() {
    AppTheme {
        ResetPasswordScreen(
            state = ResetPasswordState(),
            snackbarHostState = remember { SnackbarHostState() },
            onFormEvent = { },
            onScreenAction = { }
        )
    }
}