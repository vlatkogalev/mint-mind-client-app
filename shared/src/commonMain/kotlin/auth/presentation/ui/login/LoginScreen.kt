package auth.presentation.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MarkEmailRead
import androidx.compose.material.icons.outlined.Password
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.presentation.components.AppTopBar
import app.presentation.components.FormTextField
import app.presentation.components.PrimaryButton
import app.presentation.components.SecondaryButton
import app.presentation.components.dialog.ConfirmDialog
import app.presentation.theme.AppTheme
import app.presentation.util.calculateAdaptiveWidth
import app.presentation.util.cutoutAwarePadding
import auth.presentation.components.PasswordTextField
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.cancel
import mintmind.shared.generated.resources.ic_logo
import mintmind.shared.generated.resources.login_button_login
import mintmind.shared.generated.resources.login_button_register
import mintmind.shared.generated.resources.login_email_hint
import mintmind.shared.generated.resources.login_forgot_password
import mintmind.shared.generated.resources.login_password_hint
import mintmind.shared.generated.resources.login_verification_resend
import mintmind.shared.generated.resources.login_verification_text
import mintmind.shared.generated.resources.login_verification_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreen(
    state: LoginState,
    snackbarHostState: SnackbarHostState,
    onFormEvent: (event: LoginScreenEvent) -> Unit,
    onScreenAction: (action: LoginScreenAction) -> Unit,
) {
    val scrollState = rememberScrollState()
    val formWidth = currentWindowAdaptiveInfoV2().windowSizeClass.calculateAdaptiveWidth()

    Scaffold(
        topBar = {
            AppTopBar(
                onNavigateUp = { onScreenAction(LoginScreenAction.NavigateUp) },
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
                Image(
                    painter = painterResource(Res.drawable.ic_logo),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(192.dp),
                    contentDescription = null
                )

                FormTextField(
                    hint = stringResource(Res.string.login_email_hint),
                    value = state.email,
                    error = state.emailError?.let { stringResource(it) },
                    leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                    keyboardType = KeyboardType.Email,
                    onFormEvent = { onFormEvent(LoginScreenEvent.EmailChanged(it)) },
                    modifier = Modifier.fillMaxWidth()
                )

                PasswordTextField(
                    hint = stringResource(Res.string.login_password_hint),
                    value = state.password,
                    error = state.passwordError?.let { stringResource(it) },
                    leadingIcon = { Icon(Icons.Outlined.Password, contentDescription = null) },
                    onFormEvent = { onFormEvent(LoginScreenEvent.PasswordChanged(it)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Box(
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier
                        .height(32.dp)
                        .fillMaxWidth()
                        .clickable { onScreenAction(LoginScreenAction.NavigateToResetPassword) }
                ) {
                    Text(
                        text = stringResource(Res.string.login_forgot_password),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                PrimaryButton(
                    isLoading = state.isLoading,
                    text = stringResource(Res.string.login_button_login),
                    onButtonClick = { onFormEvent(LoginScreenEvent.Submit) },
                    modifier = Modifier.fillMaxWidth()
                )

                SecondaryButton(
                    text = stringResource(Res.string.login_button_register),
                    onButtonClick = { onScreenAction(LoginScreenAction.NavigateToRegister) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (state.showVerificationDialog) {
        ConfirmDialog(
            title = stringResource(Res.string.login_verification_title),
            text = stringResource(Res.string.login_verification_text),
            positiveButtonText = stringResource(Res.string.login_verification_resend),
            negativeButtonText = stringResource(Res.string.cancel),
            imageVector = Icons.Outlined.MarkEmailRead,
            onConfirmAction = { onFormEvent(LoginScreenEvent.ResendVerification) },
            onDismissRequest = { onFormEvent(LoginScreenEvent.DismissVerificationDialog) }
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Composable
private fun LoginScreenPreview() {
    AppTheme {
        LoginScreen(
            state = LoginState(),
            snackbarHostState = remember { SnackbarHostState() },
            onFormEvent = { },
            onScreenAction = { }
        )
    }
}