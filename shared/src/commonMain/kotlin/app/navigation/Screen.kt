package app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CenterFocusStrong
import androidx.compose.material.icons.outlined.FolderSpecial
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.collection_screen_title
import mintmind.shared.generated.resources.identify_screen_title
import org.jetbrains.compose.resources.StringResource

interface BottomNavItem {
    val icon: ImageVector
    val title: StringResource
}

@Serializable
sealed class Screen {
    @Serializable
    data object Onboarding : Screen()

    @Serializable
    data object BottomNav : Screen()

    @Serializable
    data object Home : Screen(), BottomNavItem {
        override val icon = Icons.Outlined.CenterFocusStrong
        override val title = Res.string.identify_screen_title
    }

    @Serializable
    data object Collection : Screen(), BottomNavItem {
        override val icon = Icons.Outlined.FolderSpecial
        override val title = Res.string.collection_screen_title
    }

    @Serializable
    data object Login : Screen()

    @Serializable
    data object UserCreate : Screen()

    @Serializable
    data object UserResetPassword : Screen()

    @Serializable
    data object User : Screen()

    @Serializable
    data object IdentifyGraph : Screen()

    @Serializable
    data object Identify : Screen()

    @Serializable
    data object IdentifyResult : Screen()

    @Serializable
    data class Coin(val id: String) : Screen()

    @Serializable
    data class Set(val id: String) : Screen()

    @Serializable
    data object NewsFeed : Screen()

    @Serializable
    data object CoinListings : Screen()
}