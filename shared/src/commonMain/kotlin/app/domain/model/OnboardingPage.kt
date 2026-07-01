package app.domain.model

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class OnboardingPage(
    val title: StringResource,
    val description: StringResource,
    val imageRes: DrawableResource
)