package app.presentation.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.presentation.components.PrimaryButton
import app.presentation.components.SecondaryButton
import app.presentation.theme.AppTheme
import app.presentation.util.cutoutAwarePadding
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.ic_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun OnboardingScreen(
    state: OnboardingState,
    onScreenAction: (action: OnboardingScreenAction) -> Unit,
) {
    Scaffold { paddingValues ->
        MainContent(
            paddingValues = paddingValues,
            onScreenAction = onScreenAction,
        )
    }
}

@Composable
private fun MainContent(
    paddingValues: PaddingValues,
    onScreenAction: (action: OnboardingScreenAction) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .cutoutAwarePadding(
                top = paddingValues.calculateTopPadding() + 24.dp,
                start = 36.dp,
                end = 36.dp,
                bottom = paddingValues.calculateBottomPadding() + 24.dp,
                applyStartCutoutPadding = true
            )
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_logo),
            modifier = Modifier
                .fillMaxWidth()
                .height(192.dp),
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            PrimaryButton(
                isLoading = false,
                text = "Let's get started",
                onButtonClick = { onScreenAction(OnboardingScreenAction.NavigateToHome) },
                modifier = Modifier.fillMaxWidth()
            )
            SecondaryButton(
                text = "Restore my data",
                onButtonClick = { onScreenAction(OnboardingScreenAction.NavigateToLogin) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun AuctionCreateStep3ScreenPreview() {
    AppTheme {
        OnboardingScreen(
            state = OnboardingState(),
            onScreenAction = { }
        )
    }
}