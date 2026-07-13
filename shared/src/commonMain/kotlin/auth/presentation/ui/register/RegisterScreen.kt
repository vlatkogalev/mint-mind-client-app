package auth.presentation.ui.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import app.presentation.components.AppTopBar
import app.presentation.components.BottomBarButton
import app.presentation.components.ConfirmDialog
import app.presentation.components.FormTextField
import app.presentation.components.SectionTitle
import app.presentation.components.TopAppBarText
import app.presentation.theme.AppTheme
import app.presentation.util.cutoutAwarePadding
import auth.presentation.components.PasswordTextField
import kotlinx.coroutines.delay
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.ok
import mintmind.shared.generated.resources.user_register_button_register
import mintmind.shared.generated.resources.user_register_conf_email
import mintmind.shared.generated.resources.user_register_conf_password
import mintmind.shared.generated.resources.user_register_email
import mintmind.shared.generated.resources.user_register_first_name
import mintmind.shared.generated.resources.user_register_last_name
import mintmind.shared.generated.resources.user_register_password
import mintmind.shared.generated.resources.user_register_screen_title
import mintmind.shared.generated.resources.user_register_success_message
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    state: RegisterState,
    snackbarHostState: SnackbarHostState,
    skipEnterAnimation: Boolean = false,
    onScreenEvent: (event: RegisterScreenEvent) -> Unit,
    onScreenAction: (action: RegisterScreenAction) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var showContent by remember { mutableStateOf(skipEnterAnimation) }

    LaunchedEffect(Unit) {
        if (!skipEnterAnimation) {
            delay(200)
            showContent = true
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = { TopAppBarText(text = stringResource(Res.string.user_register_screen_title)) },
                onNavigateUp = { onScreenAction(RegisterScreenAction.NavigateUp) },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            BottomBarButton(
                text = stringResource(Res.string.user_register_button_register),
                isLoading = state.isLoading,
                onClick = { onScreenEvent(RegisterScreenEvent.Submit) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(),
            modifier = Modifier.fillMaxSize()
        ) {
            MainContent(
                state = state,
                paddingValues = paddingValues,
                onScreenEvent = onScreenEvent
            )
        }
    }

    if (state.showConfirmDialog) {
        ConfirmDialog(
            title = stringResource(Res.string.user_register_screen_title),
            text = stringResource(Res.string.user_register_success_message),
            buttonText = stringResource(Res.string.ok),
            imageVector = Icons.Outlined.Lock,
            onDismissAction = {
                onScreenAction(RegisterScreenAction.ToggleConfirmDialog)
                onScreenAction(RegisterScreenAction.NavigateUp)
            },
        )
    }
}

@Composable
private fun MainContent(
    state: RegisterState,
    paddingValues: PaddingValues,
    onScreenEvent: (event: RegisterScreenEvent) -> Unit
) {
    val scrollState = rememberScrollState()
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val gridColumns = remember(windowSizeClass) {
        if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)) 2 else 1
    }
    val isLargeScreen = remember(windowSizeClass) {
        windowSizeClass.isAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND,
            WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .cutoutAwarePadding(top = paddingValues.calculateTopPadding())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(if (isLargeScreen) 0.5f else 1f)
                .align(Alignment.TopCenter)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .imePadding()
        ) {
            SectionTitle(
                title = "Email",
                modifier = Modifier.padding(top = 16.dp, start = 8.dp, end = 8.dp)
            )

            FlowRow(
                maxItemsInEachRow = gridColumns,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FormTextField(
                    hint = stringResource(Res.string.user_register_email),
                    value = state.email,
                    error = state.emailError?.let { stringResource(it) },
                    isRequired = true,
                    onFormEvent = { onScreenEvent(RegisterScreenEvent.EmailChanged(it)) },
                    keyboardType = KeyboardType.Email,
                    modifier = Modifier.weight(1f)
                )
                FormTextField(
                    hint = stringResource(Res.string.user_register_conf_email),
                    value = state.confEmail,
                    error = state.confEmailError?.let { stringResource(it) },
                    isRequired = true,
                    onFormEvent = { onScreenEvent(RegisterScreenEvent.ConfEmailChanged(it)) },
                    keyboardType = KeyboardType.Email,
                    modifier = Modifier.weight(1f)
                )
            }

            SectionTitle(
                "User Details",
                modifier = Modifier.padding(top = 16.dp, start = 8.dp, end = 8.dp)
            )

            FlowRow(
                maxItemsInEachRow = gridColumns,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FormTextField(
                    hint = stringResource(Res.string.user_register_first_name),
                    value = state.firstName,
                    error = state.firstNameError?.let { stringResource(it) },
                    isRequired = true,
                    onFormEvent = { onScreenEvent(RegisterScreenEvent.FirstNameChanged(it)) },
                    capitalization = KeyboardCapitalization.Words,
                    modifier = Modifier.weight(1f)
                )
                FormTextField(
                    hint = stringResource(Res.string.user_register_last_name),
                    value = state.lastName,
                    error = state.lastNameError?.let { stringResource(it) },
                    isRequired = true,
                    onFormEvent = { onScreenEvent(RegisterScreenEvent.LastNameChanged(it)) },
                    capitalization = KeyboardCapitalization.Words,
                    modifier = Modifier.weight(1f)
                )
            }

            SectionTitle(
                title = "Password",
                modifier = Modifier.padding(top = 16.dp, start = 8.dp, end = 8.dp)
            )

            FlowRow(
                maxItemsInEachRow = gridColumns,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                PasswordTextField(
                    hint = stringResource(Res.string.user_register_password),
                    value = state.password,
                    error = state.passwordError?.let { stringResource(it) },
                    isRequired = true,
                    onFormEvent = { onScreenEvent(RegisterScreenEvent.PasswordChanged(it)) },
                    modifier = Modifier.weight(1f)
                )

                PasswordTextField(
                    hint = stringResource(Res.string.user_register_conf_password),
                    value = state.confPassword,
                    error = state.confPasswordError?.let { stringResource(it) },
                    isRequired = true,
                    onFormEvent = { onScreenEvent(RegisterScreenEvent.ConfPasswordChanged(it)) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    AppTheme {
        RegisterScreen(
            state = RegisterState(),
            snackbarHostState = remember { SnackbarHostState() },
            skipEnterAnimation = true,
            onScreenEvent = { },
            onScreenAction = { }
        )
    }
}