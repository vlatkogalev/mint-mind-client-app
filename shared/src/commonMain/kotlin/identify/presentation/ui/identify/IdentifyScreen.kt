package identify.presentation.ui.identify

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.presentation.theme.AppTheme
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
import mintmind.shared.generated.resources.identify_screen_title
import mintmind.shared.generated.resources.identify_tap_to_close
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.abs
import kotlin.math.sqrt

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
        animationSpec = tween(durationMillis = 300),
        label = "overlayAlpha"
    )

    val spotlightRadius by animateDpAsState(
        targetValue = if (state.bothImagesCaptured) 0.dp else 100.dp,
        animationSpec = tween(durationMillis = 500, easing = EaseInOut),
        label = "spotlightRadius"
    )

    val spacerWeight by animateFloatAsState(
        targetValue = if (state.bothImagesCaptured) 0.01f else 1f,
        animationSpec = tween(500, easing = EaseInOut),
        label = "spacerWeight"
    )

    val imagesWeight by animateFloatAsState(
        targetValue = if (state.bothImagesCaptured) 1f else 0.3f,
        animationSpec = tween(500, easing = EaseInOut),
        label = "imagesWeight"
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { _ ->
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
            ) {
                IdentifyTopBar(
                    bothImagesCaptured = state.bothImagesCaptured,
                    isFlashOn = state.isFlashOn,
                    onNavigateUp = { onScreenAction(IdentifyScreenAction.NavigateUp) },
                    onToggleFlash = { onScreenAction(IdentifyScreenAction.ToggleFlash(it)) }
                )

                Spacer(modifier = Modifier.weight(spacerWeight))

                AnimatedCoinImages(
                    state = state,
                    bothImagesCaptured = state.bothImagesCaptured,
                    onImageClick = { image -> onScreenAction(IdentifyScreenAction.OnImageClick(image)) },
                    onRemoveClick = { target ->
                        onScreenAction(
                            IdentifyScreenAction.RemoveImage(
                                target
                            )
                        )
                    },
                    modifier = Modifier.weight(imagesWeight),
                )

                IdentifyBottomBar(
                    isLoading = state.isLoading,
                    isIdentifying = state.isIdentifying,
                    bothImagesCaptured = state.bothImagesCaptured,
                    onCancelIdentification = { onScreenAction(IdentifyScreenAction.CancelIdentification) },
                    onConfirmIdentification = { onScreenAction(IdentifyScreenAction.ConfirmIdentification) },
                    onCaptureImage = { onScreenAction(IdentifyScreenAction.OnCapture) }
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

@Composable
private fun IdentifyTopBar(
    bothImagesCaptured: Boolean,
    isFlashOn: Boolean,
    onNavigateUp: () -> Unit,
    onToggleFlash: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        IconButton(
            onClick = { onNavigateUp() },
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = null,
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (!bothImagesCaptured) {
            ToggleIconButton(
                isToggled = isFlashOn,
                onToggleFlash = { onToggleFlash(it) }
            )
        }
    }
}

@Composable
private fun IdentifyBottomBar(
    isLoading: Boolean,
    isIdentifying: Boolean,
    bothImagesCaptured: Boolean,
    onCancelIdentification: () -> Unit,
    onConfirmIdentification: () -> Unit,
    onCaptureImage: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        if (isLoading || isIdentifying) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.height(48.dp)
            )
        } else if (bothImagesCaptured) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                OutlinedButton(
                    onClick = { onCancelIdentification() },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color.White),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text(stringResource(Res.string.cancel))
                }

                Button(
                    onClick = { onConfirmIdentification() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text(stringResource(Res.string.identify_screen_title))
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        } else {
            Icon(
                imageVector = Icons.Default.PhotoCamera,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onCaptureImage() }
                    .padding(vertical = 16.dp)
            )
        }
    }
}

@Composable
private fun AnimatedCoinImages(
    state: IdentifyState,
    bothImagesCaptured: Boolean,
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
        ObjectImage(
            objectImage = state.obverseImage,
            objectSideName = stringResource(Res.string.identify_coin_side_obverse),
            placeholder = Res.drawable.coin_obverse,
            isCurrentTarget = state.currentCaptureTarget == CaptureTarget.OBVERSE,
            isIdentifying = state.isIdentifying,
            isCurrentScanningTarget = state.currentScanningTarget == CaptureTarget.OBVERSE,
            bothImagesCaptured = bothImagesCaptured,
            onImageClick = onImageClick,
            onRemoveClick = { onRemoveClick(CaptureTarget.OBVERSE) }
        )

        ObjectImage(
            objectImage = state.reverseImage,
            objectSideName = stringResource(Res.string.identify_coin_side_reverse),
            placeholder = Res.drawable.coin_reverse,
            isCurrentTarget = state.currentCaptureTarget == CaptureTarget.REVERSE,
            isIdentifying = state.isIdentifying,
            isCurrentScanningTarget = state.currentScanningTarget == CaptureTarget.REVERSE,
            bothImagesCaptured = bothImagesCaptured,
            onImageClick = onImageClick,
            onRemoveClick = { onRemoveClick(CaptureTarget.REVERSE) }
        )
    }
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
fun ToggleIconButton(
    isToggled: Boolean,
    onToggleFlash: (Boolean) -> Unit
) {
    IconButton(
        onClick = { onToggleFlash(!isToggled) }
    ) {
        Icon(
            imageVector = if (isToggled) Icons.Default.FlashOn else Icons.Default.FlashOff,
            contentDescription = if (isToggled) "Flash on" else "Flash off",
            tint = if (isToggled) Color.Yellow else Color.White
        )
    }
}

@Composable
fun ObjectImage(
    objectImage: ImageBitmap?,
    objectSideName: String,
    bothImagesCaptured: Boolean,
    placeholder: DrawableResource,
    isCurrentTarget: Boolean,
    isIdentifying: Boolean,
    isCurrentScanningTarget: Boolean = false,
    onImageClick: (ImageBitmap) -> Unit,
    onRemoveClick: () -> Unit,
) {
    val imageWidth by animateDpAsState(
        targetValue = if (bothImagesCaptured) 144.dp else 96.dp,
        animationSpec = tween(durationMillis = 500),
        label = "imageWidth"
    )

    val clearButtonOffset by animateDpAsState(
        targetValue = if (bothImagesCaptured) 6.dp else 0.dp,
        animationSpec = tween(durationMillis = 500),
        label = "clearButtonOffset"
    )

    Box {
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

                if (isCurrentScanningTarget && isIdentifying) {
                    ScanningOverlay(
                        isScanning = true,
                        modifier = Modifier.fillMaxSize().clip(CircleShape)
                    )
                }
            }

            Text(
                text = objectSideName,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (objectImage != null && !isIdentifying) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove image",
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