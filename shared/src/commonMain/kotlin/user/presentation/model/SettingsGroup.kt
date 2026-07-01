package user.presentation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.ContentPaste
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.outlined.Translate
import app.Const
import app.util.getPlatform
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.settings_language
import mintmind.shared.generated.resources.settings_privacy_policy
import mintmind.shared.generated.resources.settings_rate
import mintmind.shared.generated.resources.settings_share
import mintmind.shared.generated.resources.settings_terms_of_use
import mintmind.shared.generated.resources.settings_theme
import mintmind.shared.generated.resources.settings_version
import mintmind.shared.generated.resources.user_create_user
import mintmind.shared.generated.resources.user_delete_user
import mintmind.shared.generated.resources.user_logout
import mintmind.shared.generated.resources.user_password
import mintmind.shared.generated.resources.user_user_details
import user.presentation.ui.user.UserScreenAction
import user.presentation.ui.user.UserState

data class SettingsGroup(
    val items: List<SettingsItem>
) {
    companion object {
        fun create(
            state: UserState,
            onScreenAction: (UserScreenAction) -> Unit
        ): List<SettingsGroup> = buildList {
            if (state.user == null) {
                add(
                    SettingsGroup(
                        items = listOf(
                            SettingsItem(
                                title = Res.string.user_create_user,
                                leadingIcon = Icons.AutoMirrored.Outlined.Login
                            ) { onScreenAction(UserScreenAction.CreateUser) }
                        )
                    )
                )
            } else {
                add(
                    SettingsGroup(
                        items = listOf(
                            SettingsItem(
                                title = Res.string.user_user_details,
                                supportingText = state.user.email,
                                leadingIcon = Icons.Outlined.Person
                            ) { onScreenAction(UserScreenAction.ViewUserDetails) },
                            SettingsItem(
                                title = Res.string.user_password,
                                leadingIcon = Icons.Outlined.Lock
                            ) { onScreenAction(UserScreenAction.ChangePassword) },
                            SettingsItem(
                                title = Res.string.user_logout,
                                leadingIcon = Icons.AutoMirrored.Outlined.Logout
                            ) { onScreenAction(UserScreenAction.Logout) },
                            SettingsItem(
                                title = Res.string.user_delete_user,
                                leadingIcon = Icons.Outlined.Delete
                            ) { onScreenAction(UserScreenAction.DeleteUser) }
                        )
                    )
                )
            }

            add(
                SettingsGroup(
                    items = listOf(
                        SettingsItem(
                            title = Res.string.settings_language,
                            supportingText = state.language.name,
                            leadingIcon = Icons.Outlined.Translate
                        ) { onScreenAction(UserScreenAction.ToggleLanguageDialog) },
                        SettingsItem(
                            title = Res.string.settings_theme,
                            supportingText = state.theme.name,
                            leadingIcon = Icons.Outlined.Palette
                        ) { onScreenAction(UserScreenAction.ToggleThemeDialog) }
                    )
                )
            )

            add(
                SettingsGroup(
                    items = listOf(
                        SettingsItem(
                            title = Res.string.settings_privacy_policy,
                            leadingIcon = Icons.Outlined.AdminPanelSettings
                        ) { onScreenAction(UserScreenAction.ToggleRedirectDialog(Const.PRIVACY_POLICY_URL)) },
                        SettingsItem(
                            title = Res.string.settings_terms_of_use,
                            leadingIcon = Icons.Outlined.ContentPaste
                        ) { onScreenAction(UserScreenAction.ToggleRedirectDialog(Const.TERMS_OF_USE_URL)) }
                    )
                )
            )

            add(
                SettingsGroup(
                    items = listOf(
                        SettingsItem(
                            title = Res.string.settings_rate,
                            leadingIcon = Icons.Outlined.ThumbUp
                        ) { onScreenAction(UserScreenAction.RateApp) },
                        SettingsItem(
                            title = Res.string.settings_share,
                            leadingIcon = Icons.Outlined.Share
                        ) { onScreenAction(UserScreenAction.ShareApp) },
                        SettingsItem(
                            title = Res.string.settings_version,
                            supportingText = getPlatform().version,
                            leadingIcon = Icons.Outlined.Info
                        ) { }
                    )
                )
            )
        }
    }
}