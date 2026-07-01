package app.navigation.screens

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import app.navigation.Screen
import com.kashif.cameraK.permissions.Permissions
import com.kashif.cameraK.permissions.providePermissions
import identify.presentation.ui.identify.IdentifyFlowEvent
import identify.presentation.ui.identify.IdentifyResultScreen
import identify.presentation.ui.identify.IdentifyResultScreenAction
import identify.presentation.ui.identify.IdentifyScreen
import identify.presentation.ui.identify.IdentifyScreenAction
import identify.presentation.ui.identify.IdentifyViewModel
import identify.presentation.ui.identify.toIdentifyCaptureState
import identify.presentation.ui.identify.toIdentifyResultState
import io.github.aakira.napier.Napier
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.identifyScreens(
    navController: NavController
) {
    navigation<Screen.IdentifyGraph>(
        startDestination = Screen.Identify
    ) {
        composable<Screen.Identify> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.IdentifyGraph)
            }
            val identifyViewModel: IdentifyViewModel =
                koinViewModel(viewModelStoreOwner = parentEntry)
            val flowState by identifyViewModel.state.collectAsStateWithLifecycle()
            val captureState = flowState.toIdentifyCaptureState()

            val snackbarHostState = remember { SnackbarHostState() }
            val permissions: Permissions = providePermissions()

            IdentifyScreen(
                state = captureState,
                snackbarHostState = snackbarHostState
            ) { action ->
                when (action) {
                    IdentifyScreenAction.NavigateUp -> {
                        navController.navigateUp()
                    }

                    IdentifyScreenAction.ShowOffers -> {
                        // Handle show offers
                    }

                    is IdentifyScreenAction.OnCameraControllerReady -> {
                        identifyViewModel.setCameraController(action.controller)
                    }

                    is IdentifyScreenAction.CameraViewParamsChanged -> {
                        identifyViewModel.onCameraViewParametersChanged(
                            action.size,
                            action.diameter
                        )
                    }

                    is IdentifyScreenAction.ToggleFlash -> {
                        identifyViewModel.toggleFlash(action.isFlashOn)
                    }

                    IdentifyScreenAction.OnCapture -> {
                        identifyViewModel.handleImageCapture()
                    }

                    is IdentifyScreenAction.OnImageClick -> {
                        identifyViewModel.showImagePreview(action.image)
                    }

                    IdentifyScreenAction.CloseImagePreview -> {
                        identifyViewModel.closeImagePreview()
                    }

                    is IdentifyScreenAction.RemoveImage -> {
                        identifyViewModel.removeImage(action.target)
                    }

                    IdentifyScreenAction.CancelIdentification -> {
                        identifyViewModel.clearAllImages()
                    }

                    IdentifyScreenAction.ConfirmIdentification -> {
                        identifyViewModel.identifyCoin()
                    }
                }
            }

            if (!flowState.hasCameraPermission) {
                permissions.RequestCameraPermission(
                    onGranted = { identifyViewModel.toggleCameraPermission() },
                    onDenied = { Napier.e { "Camera Permission Denied" } }
                )
            }

            if (!flowState.hasStoragePermission) {
                permissions.RequestStoragePermission(
                    onGranted = { identifyViewModel.toggleStoragePermission() },
                    onDenied = { Napier.e { "Storage Permission Denied" } }
                )
            }

            LaunchedEffect(identifyViewModel, snackbarHostState) {
                identifyViewModel.events.collect { event ->
                    when (event) {
                        IdentifyFlowEvent.NavigateToResult -> {
                            navController.navigate(Screen.IdentifyResult)
                        }

                        IdentifyFlowEvent.NavigateToIdentifyFlow -> Unit

                        is IdentifyFlowEvent.ShowMessage -> {
                            snackbarHostState.showSnackbar(
                                message = event.message,
                                duration = SnackbarDuration.Long,
                                withDismissAction = true
                            )
                        }
                    }
                }
            }
        }

        composable<Screen.IdentifyResult> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.IdentifyGraph)
            }
            val identifyViewModel: IdentifyViewModel =
                koinViewModel(viewModelStoreOwner = parentEntry)
            val flowState by identifyViewModel.state.collectAsStateWithLifecycle()
            val state = flowState.toIdentifyResultState()

            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(identifyViewModel, snackbarHostState) {
                identifyViewModel.events.collect { event ->
                    when (event) {
                        IdentifyFlowEvent.NavigateToResult -> Unit

                        IdentifyFlowEvent.NavigateToIdentifyFlow -> {
                            navController.navigateUp()
                        }

                        is IdentifyFlowEvent.ShowMessage -> {
                            snackbarHostState.showSnackbar(
                                message = event.message,
                                duration = SnackbarDuration.Long,
                                withDismissAction = true
                            )
                        }
                    }
                }
            }

            IdentifyResultScreen(
                state = state,
                snackbarHostState = snackbarHostState,
                onScreenAction = { action ->
                    when (action) {
                        IdentifyResultScreenAction.NavigateUp -> {
                            identifyViewModel.scanAgain()
                        }

                        IdentifyResultScreenAction.SaveToCollection -> {
                            identifyViewModel.addToCollection()
                        }
                    }
                },
            )
        }
    }
}
