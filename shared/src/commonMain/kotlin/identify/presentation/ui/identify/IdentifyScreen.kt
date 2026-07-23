package identify.presentation.ui.identify

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateBounds
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingToolbarColors
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalFloatingToolbar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import app.presentation.components.AppTopBar
import app.presentation.theme.AppTheme
import app.presentation.util.cutoutAwarePadding
import com.kashif.cameraK.compose.CameraPreviewView
import com.kashif.cameraK.compose.rememberCameraKState
import com.kashif.cameraK.enums.CameraLens
import com.kashif.cameraK.enums.Directory
import com.kashif.cameraK.enums.FlashMode
import com.kashif.cameraK.enums.ImageFormat
import com.kashif.cameraK.enums.QualityPrioritization
import com.kashif.cameraK.enums.TorchMode
import com.kashif.cameraK.state.CameraConfiguration
import com.kashif.cameraK.state.CameraKState
import identify.domain.model.CaptureTarget
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.cancel
import mintmind.shared.generated.resources.coin_obverse
import mintmind.shared.generated.resources.coin_reverse
import mintmind.shared.generated.resources.identify_coin_side_obverse
import mintmind.shared.generated.resources.identify_coin_side_reverse
import mintmind.shared.generated.resources.identify_flash_off
import mintmind.shared.generated.resources.identify_flash_on
import mintmind.shared.generated.resources.identify_remove_image
import mintmind.shared.generated.resources.identify_screen_title
import mintmind.shared.generated.resources.identify_tap_to_close
import mintmind.shared.generated.resources.identify_torch_off
import mintmind.shared.generated.resources.identify_torch_on
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.abs
import kotlin.math.sqrt

private object IdentifyScreenDefaults {
    val SpotlightRadius = 100.dp
    const val OverlayFadeDurationMillis = 300
    const val TransitionDurationMillis = 500
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IdentifyScreen(
    state: IdentifyState,
    snackbarHostState: SnackbarHostState,
    onScreenAction: (IdentifyScreenAction) -> Unit
) {
    val density = LocalDensity.current
    val cameraState by rememberCameraKState(
        config = CameraConfiguration(
            cameraLens = CameraLens.BACK,
            flashMode = FlashMode.OFF,
            imageFormat = ImageFormat.JPEG,
            directory = Directory.DOCUMENTS,
            torchMode = TorchMode.OFF,
            qualityPrioritization = QualityPrioritization.QUALITY,
            returnFilePath = true
        )
    )
    val cameraController = (cameraState as? CameraKState.Ready)?.controller

    LaunchedEffect(cameraController) {
        if (cameraController != null) {
            onScreenAction(IdentifyScreenAction.OnCameraControllerReady(cameraController))
        }
    }

    val overlayAlpha by animateFloatAsState(
        targetValue = if (state.bothImagesCaptured) 0.5f else 0f,
        animationSpec = tween(durationMillis = IdentifyScreenDefaults.OverlayFadeDurationMillis),
        label = "overlayAlpha"
    )

    val spotlightRadius by animateDpAsState(
        targetValue = if (state.bothImagesCaptured) 0.dp else IdentifyScreenDefaults.SpotlightRadius,
        animationSpec = tween(
            durationMillis = IdentifyScreenDefaults.TransitionDurationMillis,
            easing = EaseInOut
        ),
        label = "spotlightRadius"
    )

    // Phone landscape: not enough height to stack the controls, so they move to the side edges.
    val isCompactHeight = !currentWindowAdaptiveInfoV2().windowSizeClass
        .isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)

    Scaffold(
        topBar = {
            AppTopBar(
                onNavigateUp = { onScreenAction(IdentifyScreenAction.NavigateUp) },
                transparent = true,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (cameraController != null) {
                CameraPreviewView(
                    controller = cameraController,
                    modifier = Modifier
                        .fillMaxSize()
                        .onSizeChanged { size ->
                            val diameter = with(density) { (spotlightRadius * 2).roundToPx() }
                            onScreenAction(
                                IdentifyScreenAction.CameraViewParamsChanged(
                                    size,
                                    diameter
                                )
                            )
                        }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                )
            }

            CameraSpotlight(spotlightRadius = spotlightRadius, overlayAlpha = overlayAlpha)

            val contentModifier = Modifier
                .fillMaxSize()
                .cutoutAwarePadding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )

            if (isCompactHeight) {
                IdentifyLandscapeContent(
                    state = state,
                    onScreenAction = onScreenAction,
                    modifier = contentModifier
                )
            } else {
                IdentifyPortraitContent(
                    state = state,
                    onScreenAction = onScreenAction,
                    modifier = contentModifier
                )
            }

            if (state.showImagePreview && state.previewImage != null) {
                ImagePreviewDialog(
                    image = state.previewImage,
                    onDismiss = { onScreenAction(IdentifyScreenAction.CloseImagePreview) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun IdentifyPortraitContent(
    state: IdentifyState,
    onScreenAction: (IdentifyScreenAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacerWeight by animateFloatAsState(
        targetValue = if (state.bothImagesCaptured) 0.01f else 1f,
        animationSpec = tween(IdentifyScreenDefaults.TransitionDurationMillis, easing = EaseInOut),
        label = "spacerWeight"
    )

    val imagesWeight by animateFloatAsState(
        targetValue = if (state.bothImagesCaptured) 1f else 0.3f,
        animationSpec = tween(IdentifyScreenDefaults.TransitionDurationMillis, easing = EaseInOut),
        label = "imagesWeight"
    )

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.weight(spacerWeight))

        AnimatedCoinImages(
            state = state,
            onImageClick = { image -> onScreenAction(IdentifyScreenAction.OnImageClick(image)) },
            onRemoveClick = { target ->
                onScreenAction(IdentifyScreenAction.RemoveImage(target))
            },
            modifier = Modifier.weight(imagesWeight),
        )

        IdentifyFloatingToolbar(
            state = state,
            onScreenAction = onScreenAction,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(
                    top = 16.dp,
                    bottom = FloatingToolbarDefaults.ScreenOffset
                )
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun IdentifyLandscapeContent(
    state: IdentifyState,
    onScreenAction: (IdentifyScreenAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        // FlowColumn wraps into a second column when the enlarged captured images
        // exceed the compact window height, placing them side by side instead.
        // LookaheadScope + animateBounds animate that reflow instead of jumping,
        // and also animate the glide from the start edge to center on capture,
        // since the alignment switch happens inside the scope.
        LookaheadScope {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp)
            ) {
                FlowColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier
                        .align(
                            if (state.bothImagesCaptured) {
                                Alignment.Center
                            } else {
                                Alignment.CenterStart
                            }
                        )
                        .fillMaxHeight()
                ) {
                    CaptureTarget.entries.forEach { target ->
                        CoinSideImage(
                            state = state,
                            target = target,
                            onImageClick = { image ->
                                onScreenAction(IdentifyScreenAction.OnImageClick(image))
                            },
                            onRemoveClick = {
                                onScreenAction(IdentifyScreenAction.RemoveImage(it))
                            },
                            modifier = Modifier.animateBounds(
                                lookaheadScope = this@LookaheadScope,
                                boundsTransform = { _, _ ->
                                    tween(
                                        durationMillis = IdentifyScreenDefaults.TransitionDurationMillis,
                                        easing = EaseInOut
                                    )
                                }
                            )
                        )
                    }
                }
            }
        }

        IdentifyVerticalFloatingToolbar(
            state = state,
            onScreenAction = onScreenAction,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = FloatingToolbarDefaults.ScreenOffset)
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun IdentifyFloatingToolbar(
    state: IdentifyState,
    onScreenAction: (IdentifyScreenAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        HorizontalFloatingToolbar(
            expanded = true,
            colors = FloatingToolbarColors(
                toolbarContainerColor = MaterialTheme.colorScheme.primaryContainer,
                toolbarContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                fabContainerColor = MaterialTheme.colorScheme.primary,
                fabContentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            AnimatedContent(
                targetState = state.bothImagesCaptured,
                label = "toolbarContent"
            ) { bothCaptured ->
                if (bothCaptured) {
                    CancelButton(
                        enabled = !state.isBusy,
                        onClick = { onScreenAction(IdentifyScreenAction.CancelIdentification) }
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CameraControlButtons(
                            state = state,
                            enabled = !state.isBusy,
                            onScreenAction = onScreenAction
                        )
                    }
                }
            }
        }

        IdentifyFab(state = state, onScreenAction = onScreenAction)
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun IdentifyVerticalFloatingToolbar(
    state: IdentifyState,
    onScreenAction: (IdentifyScreenAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        VerticalFloatingToolbar(
            expanded = true,
            colors = FloatingToolbarColors(
                toolbarContainerColor = MaterialTheme.colorScheme.primaryContainer,
                toolbarContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                fabContainerColor = MaterialTheme.colorScheme.tertiary,
                fabContentColor = MaterialTheme.colorScheme.onTertiary
            )
        ) {
            AnimatedContent(
                targetState = state.bothImagesCaptured,
                label = "toolbarContent"
            ) { bothCaptured ->
                if (bothCaptured) {
                    CancelButton(
                        enabled = !state.isBusy,
                        onClick = { onScreenAction(IdentifyScreenAction.CancelIdentification) }
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CameraControlButtons(
                            state = state,
                            enabled = !state.isBusy,
                            onScreenAction = onScreenAction
                        )
                    }
                }
            }
        }

        // Last in the stack so it sits closest to the bottom corner, within thumb reach.
        IdentifyFab(state = state, onScreenAction = onScreenAction)
    }
}

@Composable
private fun CameraControlButtons(
    state: IdentifyState,
    enabled: Boolean,
    onScreenAction: (IdentifyScreenAction) -> Unit
) {
    ToolbarToggleButton(
        checked = state.isTorchOn,
        enabled = enabled,
        checkedIcon = Icons.Default.FlashlightOn,
        uncheckedIcon = Icons.Default.FlashlightOff,
        checkedDescription = stringResource(Res.string.identify_torch_on),
        uncheckedDescription = stringResource(Res.string.identify_torch_off),
        onCheckedChange = {
            onScreenAction(IdentifyScreenAction.ToggleTorch(it))
        }
    )

    ToolbarToggleButton(
        checked = state.isFlashOn,
        enabled = enabled,
        checkedIcon = Icons.Default.FlashOn,
        uncheckedIcon = Icons.Default.FlashOff,
        checkedDescription = stringResource(Res.string.identify_flash_on),
        uncheckedDescription = stringResource(Res.string.identify_flash_off),
        onCheckedChange = {
            onScreenAction(IdentifyScreenAction.ToggleFlash(it))
        }
    )

    IconButton(
        enabled = enabled,
        onClick = { onScreenAction(IdentifyScreenAction.CycleZoom) }
    ) {
        Text(
            text = state.zoomLabel,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun CancelButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(Res.string.cancel)
        )
    }
}

@Composable
private fun IdentifyFab(
    state: IdentifyState,
    onScreenAction: (IdentifyScreenAction) -> Unit
) {
    val fabState = state.fabState

    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        onClick = {
            when (fabState) {
                IdentifyFabState.Capture -> onScreenAction(IdentifyScreenAction.OnCapture)
                IdentifyFabState.Identify -> onScreenAction(IdentifyScreenAction.ConfirmIdentification)
                IdentifyFabState.Busy -> Unit
            }
        }
    ) {
        AnimatedContent(
            targetState = fabState,
            label = "fabContent"
        ) { targetFabState ->
            when (targetFabState) {
                IdentifyFabState.Capture -> {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = null
                    )
                }

                IdentifyFabState.Identify -> {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(Res.string.identify_screen_title)
                    )
                }

                IdentifyFabState.Busy -> {
                    CircularProgressIndicator(
                        color = LocalContentColor.current,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ToolbarToggleButton(
    checked: Boolean,
    checkedIcon: ImageVector,
    uncheckedIcon: ImageVector,
    checkedDescription: String,
    uncheckedDescription: String,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    FilledIconToggleButton(
        checked = checked,
        enabled = enabled,
        onCheckedChange = onCheckedChange,
        colors = IconButtonDefaults.filledIconToggleButtonColors(
            containerColor = Color.Transparent,
            contentColor = LocalContentColor.current,
            checkedContentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Icon(
            imageVector = if (checked) checkedIcon else uncheckedIcon,
            contentDescription = if (checked) checkedDescription else uncheckedDescription
        )
    }
}

@Composable
private fun AnimatedCoinImages(
    state: IdentifyState,
    onImageClick: (ImageBitmap) -> Unit,
    onRemoveClick: (CaptureTarget) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(
            space = 24.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalArrangement = Arrangement.spacedBy(
            space = 24.dp,
            alignment = Alignment.CenterVertically
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        CaptureTarget.entries.forEach { target ->
            CoinSideImage(
                state = state,
                target = target,
                onImageClick = onImageClick,
                onRemoveClick = onRemoveClick
            )
        }
    }
}

@Composable
private fun CoinSideImage(
    state: IdentifyState,
    target: CaptureTarget,
    onImageClick: (ImageBitmap) -> Unit,
    onRemoveClick: (CaptureTarget) -> Unit,
    modifier: Modifier = Modifier
) {
    val image = state.imageFor(target)

    ObjectImage(
        modifier = modifier,
        objectImage = image,
        objectSideName = stringResource(target.sideNameRes),
        placeholder = target.placeholderRes,
        isCurrentTarget = state.isCurrentCaptureTarget(target),
        isScanning = state.isScanningTarget(target),
        enlarged = state.bothImagesCaptured,
        showRemoveButton = image != null && !state.isIdentifying,
        onImageClick = onImageClick,
        onRemoveClick = { onRemoveClick(target) }
    )
}

private val CaptureTarget.sideNameRes: StringResource
    get() = when (this) {
        CaptureTarget.OBVERSE -> Res.string.identify_coin_side_obverse
        CaptureTarget.REVERSE -> Res.string.identify_coin_side_reverse
    }

private val CaptureTarget.placeholderRes: DrawableResource
    get() = when (this) {
        CaptureTarget.OBVERSE -> Res.drawable.coin_obverse
        CaptureTarget.REVERSE -> Res.drawable.coin_reverse
    }

@Composable
private fun ImagePreviewDialog(
    image: ImageBitmap,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                bitmap = image,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.8f)
                    .clip(RoundedCornerShape(12.dp))
            )

            Text(
                text = stringResource(Res.string.identify_tap_to_close),
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun CameraSpotlight(
    spotlightRadius: Dp,
    overlayAlpha: Float
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (spotlightRadius > 0.dp) {
            val radiusInPx = spotlightRadius.toPx()

            val circlePath = Path().apply {
                addOval(Rect(center, radiusInPx))
            }

            clipPath(circlePath, clipOp = ClipOp.Difference) {
                drawRect(SolidColor(Color.Black.copy(alpha = 0.5f)))
            }
        } else if (overlayAlpha > 0f) {
            drawRect(SolidColor(Color.Black.copy(alpha = overlayAlpha)))
        }
    }
}

@Composable
fun ObjectImage(
    objectImage: ImageBitmap?,
    objectSideName: String,
    placeholder: DrawableResource,
    modifier: Modifier = Modifier,
    isCurrentTarget: Boolean,
    isScanning: Boolean,
    enlarged: Boolean,
    showRemoveButton: Boolean,
    onImageClick: (ImageBitmap) -> Unit,
    onRemoveClick: () -> Unit,
) {
    val imageWidth by animateDpAsState(
        targetValue = if (enlarged) 144.dp else 96.dp,
        animationSpec = tween(durationMillis = IdentifyScreenDefaults.TransitionDurationMillis),
        label = "imageWidth"
    )

    val clearButtonOffset by animateDpAsState(
        targetValue = if (enlarged) 6.dp else 0.dp,
        animationSpec = tween(durationMillis = IdentifyScreenDefaults.TransitionDurationMillis),
        label = "clearButtonOffset"
    )

    Box(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.width(imageWidth)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                objectImage?.let { imageBitmap ->
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = objectSideName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .border(
                                width = if (isCurrentTarget) 5.dp else 1.dp,
                                color = Color.White,
                                shape = CircleShape
                            )
                            .clickable { onImageClick(imageBitmap) }
                    )
                } ?: run {
                    Image(
                        painter = painterResource(placeholder),
                        contentDescription = objectSideName,
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .border(
                                width = if (isCurrentTarget) 5.dp else 1.dp,
                                color = Color.White,
                                shape = CircleShape
                            )
                            .padding(4.dp)
                    )
                }

                if (isScanning) {
                    ScanningOverlay(
                        isScanning = true,
                        modifier = Modifier.fillMaxSize().clip(CircleShape)
                    )
                }
            }

            Text(
                text = objectSideName,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (showRemoveButton) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(Res.string.identify_remove_image),
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
                    .offset(x = -clearButtonOffset, y = clearButtonOffset)
                    .background(color = Color.DarkGray, shape = CircleShape)
                    .padding(4.dp)
                    .clickable { onRemoveClick() }
            )
        }
    }
}

@Composable
fun ScanningOverlay(
    isScanning: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (!isScanning) return

    val infiniteTransition = rememberInfiniteTransition(label = "scanningTransition")

    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scanningProgress"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val radius = size.minDimension / 2
        val center = Offset(size.width / 2, size.height / 2)

        val lineY = (radius * 2 * animatedProgress) - radius

        if (lineY >= -radius && lineY <= radius) {
            val distanceFromCenter = abs(lineY)
            val lineHalfWidth = sqrt((radius * radius) - (distanceFromCenter * distanceFromCenter))

            val lineStart = Offset(center.x - lineHalfWidth, center.y + lineY)
            val lineEnd = Offset(center.x + lineHalfWidth, center.y + lineY)

            drawLine(
                color = Color.Red.copy(alpha = 0.5f),
                start = lineStart,
                end = lineEnd,
                strokeWidth = 4.dp.toPx(),
                cap = StrokeCap.Round
            )

            drawLine(
                color = Color.Red.copy(alpha = 0.25f),
                start = lineStart,
                end = lineEnd,
                strokeWidth = 10.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

@Preview
@Composable
fun IdentifyScreenPreview() {
    AppTheme {
        IdentifyScreen(
            state = IdentifyState(),
            snackbarHostState = remember { SnackbarHostState() },
            onScreenAction = {}
        )
    }
}