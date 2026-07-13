package identify.presentation.ui.identify

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.presentation.components.AppTopBar
import app.presentation.theme.AppTheme
import app.presentation.theme.AppThemeExt
import app.presentation.util.calculateGridConfig
import app.presentation.util.cutoutAwarePaddingValues
import identify.data.remote.dto.CoinAnalysisDto
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.coin_details_historical_context
import mintmind.shared.generated.resources.coin_details_lettering
import mintmind.shared.generated.resources.coin_details_market_demand
import mintmind.shared.generated.resources.coin_details_supply_analysis
import mintmind.shared.generated.resources.coin_placeholder
import mintmind.shared.generated.resources.identify_coin_side_obverse
import mintmind.shared.generated.resources.identify_coin_side_reverse
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

@Composable
fun IdentifyResultScreen(
    state: IdentifyResultState,
    snackbarHostState: SnackbarHostState,
    onScreenAction: (IdentifyResultScreenAction) -> Unit,
) {
    Scaffold(
        topBar = {
            AppTopBar(
                onNavigateUp = { onScreenAction(IdentifyResultScreenAction.NavigateUp) },
                transparent = true,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        MainContent(
            state = state,
            paddingValues = paddingValues,
            onScreenAction = onScreenAction,
        )
    }
}

@Composable
private fun MainContent(
    state: IdentifyResultState,
    paddingValues: PaddingValues,
    onScreenAction: (IdentifyResultScreenAction) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val gridConfig = windowSizeClass.calculateGridConfig(1, 2, 3)

    LazyVerticalGrid(
        columns = gridConfig.gridCells,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = cutoutAwarePaddingValues(
            top = paddingValues.calculateTopPadding() + 16.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = paddingValues.calculateBottomPadding() + 16.dp,
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            ObjectVisuals(
                obverseImage = state.obverseImage,
                reverseImage = state.reverseImage,
                modifier = Modifier.animateItem()
            )
        }

        item {
            val name = state.objectDetails?.identification?.denomination ?: "N/A"
            val year = state.objectDetails?.identification?.year ?: 0

            Text(
                text = "$name - $year",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            val rarityScore = state.objectDetails?.rarity?.score
            val rarityIndex = rarityScore?.let { "${(it * 10).roundToInt()}/10" } ?: "N/A"

            CoinRarityAndGrading(
                grade = state.objectDetails?.condition?.grade?.abbreviation ?: "N/A",
                gradeInfo = state.objectDetails?.condition?.grade?.name ?: "N/A",
                mintage = state.objectDetails?.rarity?.mintage?.toString() ?: "N/A",
                rarityIndex = rarityIndex,
                rarity = state.objectDetails?.rarity?.classification ?: "N/A",
                modifier = Modifier.animateItem()
            )
        }

        item {
            CoinDetailsItem(
                title = stringResource(Res.string.coin_details_supply_analysis),
                value = state.objectDetails?.market?.supplySummary ?: "N/A",
                modifier = Modifier.animateItem()
            )
        }

        item {
            CoinDetailsItem(
                title = stringResource(Res.string.coin_details_market_demand),
                value = state.objectDetails?.market?.demandSummary ?: "N/A",
                modifier = Modifier.animateItem()
            )
        }

        item {
            val designer = listOfNotNull(
                state.objectDetails?.specifications?.designer?.obverse,
                state.objectDetails?.specifications?.designer?.reverse
            )

            CoinTechnicalDetails(
                diameter = state.objectDetails?.specifications?.diameterMm?.toString() ?: "N/A",
                thickness = state.objectDetails?.specifications?.thicknessMm?.toString() ?: "N/A",
                weight = state.objectDetails?.specifications?.weightGrams?.toString() ?: "N/A",
                edge = state.objectDetails?.specifications?.edge ?: "N/A",
                composition = state.objectDetails?.specifications?.composition ?: "N/A",
                designer = designer,
                modifier = Modifier.animateItem()
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CoinDetailsItem(
                    title = stringResource(Res.string.identify_coin_side_obverse),
                    value = state.objectDetails?.design?.obverse?.description ?: "N/A",
                    modifier = Modifier.animateItem()
                )
                CoinDetailsItem(
                    title = stringResource(Res.string.coin_details_lettering),
                    value = state.objectDetails?.design?.obverse?.lettering ?: "N/A",
                    modifier = Modifier.animateItem()
                )
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CoinDetailsItem(
                    title = stringResource(Res.string.identify_coin_side_reverse),
                    value = state.objectDetails?.design?.reverse?.description ?: "N/A",
                    modifier = Modifier.animateItem()
                )
                CoinDetailsItem(
                    title = stringResource(Res.string.coin_details_lettering),
                    value = state.objectDetails?.design?.reverse?.lettering ?: "N/A",
                    modifier = Modifier.animateItem()
                )
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            CoinDetailsItem(
                title = stringResource(Res.string.coin_details_historical_context),
                value = state.objectDetails?.historicalContext ?: "N/A",
                modifier = Modifier.animateItem()
            )
        }
//
        item(span = { GridItemSpan(maxLineSpan) }) {
            ResultActions(
                isLoading = state.isLoading,
                onAddToCollection = { onScreenAction(IdentifyResultScreenAction.SaveToCollection) },
                onScanAgain = { onScreenAction(IdentifyResultScreenAction.NavigateUp) },
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Composable
private fun ObjectVisuals(
    obverseImage: ImageBitmap?,
    reverseImage: ImageBitmap?,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(horizontal = 32.dp, vertical = 8.dp)
    ) {
        CoinSideImage(
            image = obverseImage,
            contentDescription = stringResource(Res.string.identify_coin_side_obverse),
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        CoinSideImage(
            image = reverseImage,
            contentDescription = stringResource(Res.string.identify_coin_side_reverse),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun CoinSideImage(
    image: ImageBitmap?,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    if (image != null) {
        Image(
            bitmap = image,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(CircleShape)
        )
    } else {
        Image(
            painter = painterResource(Res.drawable.coin_placeholder),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(CircleShape)
        )
    }
}

@Composable
private fun ResultActions(
    isLoading: Boolean,
    onAddToCollection: () -> Unit,
    onScanAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedButton(
            enabled = !isLoading,
            onClick = onScanAgain,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Scan Again")
        }

        Button(
            enabled = !isLoading,
            onClick = onAddToCollection,
            modifier = Modifier.weight(1f)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(20.dp)
                        .width(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(text = "Add to Collection")
            }
        }
    }
}

@Composable
private fun CoinRarityAndGrading(
    grade: String,
    gradeInfo: String,
    mintage: String,
    rarityIndex: String,
    rarity: String,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            CoinDetailTitleAndValue(
                title = "Sheldon Scale",
                value = grade,
                additionalInfo = gradeInfo,
                isMainDetail = true
            )
            CoinDetailTitleAndValue(
                title = "Mintage",
                value = mintage
            )
            CoinDetailTitleAndValue(
                title = "Rarity Index",
                value = rarityIndex,
                additionalInfo = rarity
            )
        }
    }
}

@Composable
private fun CoinDetailTitleAndValue(
    title: String,
    value: String,
    additionalInfo: String? = null,
    isMainDetail: Boolean = false,
    modifier: Modifier = Modifier
) {
    val color = if (isMainDetail)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.onSurface

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = AppThemeExt.colors.textSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color,
        )
        if (additionalInfo != null) {
            Text(
                text = additionalInfo,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun CoinTechnicalDetails(
    diameter: String,
    thickness: String,
    weight: String,
    edge: String,
    composition: String,
    designer: List<String>,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                CoinDetailsItem(
                    title = "Edge",
                    value = edge,
                    modifier = Modifier.weight(1f)
                )
                CoinDetailsItem(
                    title = "Diameter",
                    value = "$diameter mm",
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                CoinDetailsItem(
                    title = "Thickness",
                    value = "$thickness mm",
                    modifier = Modifier.weight(1f)
                )
                CoinDetailsItem(
                    title = "Weight",
                    value = "$weight g",
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                CoinDetailsItem(
                    title = "Composition",
                    value = composition,
                    modifier = Modifier.weight(1f)
                )
                CoinDetailsItem(
                    title = "Designer",
                    value = designer.joinToString(", "),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CoinDetailsItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = AppThemeExt.colors.textSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview
@Composable
fun IdentifyResultScreenPreview() {
    AppTheme {
        IdentifyResultScreen(
            state = IdentifyResultState(objectDetails = CoinAnalysisDto.sampleAnalysis),
            snackbarHostState = remember { SnackbarHostState() },
            onScreenAction = {},
        )
    }
}
