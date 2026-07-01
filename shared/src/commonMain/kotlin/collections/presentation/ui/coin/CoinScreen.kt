package collections.presentation.ui.coin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import app.presentation.components.PrimaryButton
import app.presentation.components.ReededDivider
import app.presentation.components.SecondaryButton
import app.presentation.theme.AppTheme
import app.presentation.theme.AppThemeExt
import coil3.compose.AsyncImage
import collections.presentation.mapper.dummyCoinUi
import collections.presentation.model.CoinUiModel
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.coin_placeholder
import mintmind.shared.generated.resources.identify_coin_side_obverse
import mintmind.shared.generated.resources.identify_coin_side_reverse
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun CoinScreen(
    state: CoinState,
    snackbarHostState: SnackbarHostState,
    onScreenAction: (CoinScreenAction) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { onScreenAction(CoinScreenAction.NavigateUp) }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, null)
                    }
                },
                colors = TopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.tertiary,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.tertiary,
                    subtitleContentColor = AppThemeExt.colors.textSecondary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        MainContent(
            state = state,
            paddingValues = paddingValues
        )
    }
}

@Composable
private fun MainContent(
    state: CoinState,
    paddingValues: PaddingValues
) {
    state.coin?.let { coin ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding() + 24.dp
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                HeroSection(coin = coin)
            }

            item { ReededDivider(Modifier.padding(16.dp)) }

            item {
                QuickFactsRow(
                    grade = coin.grade,
                    gradeName = coin.gradeName,
                    valueRange = coin.valueRange,
                    valueSubtitle = coin.valueSubtitle,
                    rarity = coin.rarity,
                    rarityFraction = coin.rarityFraction,
                )
            }

            item { ReededDivider(Modifier.padding(16.dp)) }

            item {
                SectionContainer {
                    Eyebrow(text = "Identification")
                    FieldRow(label = "Country / Issuer", value = coin.country)
                    Spacer(Modifier.height(12.dp))
                    FieldRow(label = "Series", value = coin.series)
                    Spacer(Modifier.height(12.dp))
                    FieldRow(label = "Year", value = coin.year)
                    Spacer(Modifier.height(12.dp))
                    FieldRow(label = "Era", value = coin.era)
                    if (coin.mintMark.isNotEmpty()) {
                        Spacer(Modifier.height(12.dp))
                        FieldRow(label = "Mint mark", value = coin.mintMark)
                    }
                }
            }

            item { ReededDivider(Modifier.padding(16.dp)) }

            item {
                SectionContainer {
                    Eyebrow(text = "Obverse")
                    if (coin.obverseDescription.isNotEmpty()) {
                        BodyText(coin.obverseDescription)
                    }
                    if (coin.obverseLettering.isNotEmpty()) {
                        InscriptionBlock(coin.obverseLettering)
                    }
                    if (coin.obverseDesigner.isNotEmpty()) {
                        DesignerLabel("Designer — ${coin.obverseDesigner}")
                    }
                }
            }

            item { ReededDivider(Modifier.padding(16.dp)) }

            item {
                SectionContainer {
                    Eyebrow(text = "Reverse")
                    if (coin.reverseDescription.isNotEmpty()) {
                        BodyText(coin.reverseDescription)
                    }
                    if (coin.reverseLettering.isNotEmpty()) {
                        InscriptionBlock(coin.reverseLettering)
                    }
                    if (coin.reverseDesigner.isNotEmpty()) {
                        DesignerLabel("Designer — ${coin.reverseDesigner}")
                    }
                }
            }

            item { ReededDivider(Modifier.padding(16.dp)) }

            item {
                SectionContainer {
                    Eyebrow(text = "Specification")
                    SpecGrid(specs = coin.specs)
                }
            }

            item { ReededDivider(Modifier.padding(16.dp)) }

            if (coin.catalogueChips.isNotEmpty() || coin.numistaUrl.isNotEmpty()) {
                item {
                    CatalogueSection(
                        chips = coin.catalogueChips,
                        numistaUrl = coin.numistaUrl,
                    )
                }
                item { ReededDivider(Modifier.padding(16.dp)) }
            }

            if (coin.positiveFeatures.isNotEmpty() || coin.negativeFeatures.isNotEmpty()) {
                item {
                    ConditionSection(
                        positives = coin.positiveFeatures,
                        negatives = coin.negativeFeatures,
                    )
                }
                item { ReededDivider(Modifier.padding(16.dp)) }
            }

            item {
                ContextSection(
                    contextBody = coin.contextBody,
                    tags = coin.tags,
                    valueDisclaimer = coin.valueDisclaimer,
                )
            }

            item { ReededDivider(Modifier.padding(16.dp)) }

            item {
                ActionButtons(
                    onEditNotes = { /* TODO */ },
                    onAddToSet = { /* TODO */ },
                )
            }
        }
    }
}

@Composable
private fun HeroSection(coin: CoinUiModel) {
    val copperColor = MaterialTheme.colorScheme.tertiary
    val surfaceColor = MaterialTheme.colorScheme.surfaceContainerHigh
    val backgroundColor = MaterialTheme.colorScheme.background

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxWidth()
            .drawWithCache {
                val radius = 550f

                val brush = Brush.radialGradient(
                    colors = listOf(surfaceColor, backgroundColor),
                    center = size.center,
                    radius = radius
                )

                onDrawBehind {
                    drawCircle(
                        brush = brush,
                        center = size.center,
                        radius = radius
                    )
                }
            }
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Box(
                modifier = Modifier.height(116.dp)
            ) {
                AsyncImage(
                    model = coin.obverseImageUrl,
                    contentDescription = stringResource(Res.string.identify_coin_side_obverse),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(Res.drawable.coin_placeholder),
                    error = painterResource(Res.drawable.coin_placeholder),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(104.dp)
                        .offset(x = (-44).dp)
                        .zIndex(1f)
                        .clip(CircleShape)
                        .border(2.dp, copperColor, CircleShape)
                )
                AsyncImage(
                    model = coin.reverseImageUrl,
                    contentDescription = stringResource(Res.string.identify_coin_side_reverse),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(Res.drawable.coin_placeholder),
                    error = painterResource(Res.drawable.coin_placeholder),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(104.dp)
                        .offset(x = 44.dp)
                        .clip(CircleShape)
                        .border(2.dp, copperColor, CircleShape)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = coin.denomination,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = coin.subtitle,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppThemeExt.colors.textSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(Modifier.height(14.dp))

            val (badgeBg, badgeFg) = when (coin.confidenceLevel) {
                CoinUiModel.ConfidenceLevel.HIGH -> AppThemeExt.statusColors.verifiedContainer to AppThemeExt.statusColors.verified
                CoinUiModel.ConfidenceLevel.MEDIUM -> AppThemeExt.statusColors.needsReviewContainer to AppThemeExt.statusColors.needsReview
                CoinUiModel.ConfidenceLevel.LOW -> AppThemeExt.statusColors.notFoundContainer to AppThemeExt.statusColors.notFound
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clip(CircleShape)
                    .background(badgeBg)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = badgeFg,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = coin.confidenceLabel,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = badgeFg
                )
            }
        }
    }
}

@Composable
private fun QuickFactsRow(
    grade: String,
    gradeName: String,
    valueRange: String,
    valueSubtitle: String,
    rarity: String,
    rarityFraction: Float,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    ) {
        FactCell(
            modifier = Modifier.weight(1f),
            borderEnd = true
        ) {
            FactEyebrow("Grade")
            Text(
                grade,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                gradeName,
                style = MaterialTheme.typography.labelSmall,
                color = AppThemeExt.colors.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        FactCell(modifier = Modifier.weight(1f), borderEnd = true) {
            FactEyebrow("Value")
            Text(
                valueRange,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                valueSubtitle,
                style = MaterialTheme.typography.labelSmall,
                color = AppThemeExt.colors.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        FactCell(modifier = Modifier.weight(1f), borderEnd = false) {
            FactEyebrow("Rarity")
            Text(
                rarity,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )
            Box(
                modifier = Modifier.height(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                LinearProgressIndicator(
                    progress = { rarityFraction },
                    color = MaterialTheme.colorScheme.tertiary,
                    trackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    drawStopIndicator = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp))
                )
            }
        }
    }
}

@Composable
private fun FactCell(
    modifier: Modifier = Modifier,
    borderEnd: Boolean,
    content: @Composable () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxHeight()
            .then(
                if (borderEnd) Modifier.border(
                    width = 0.dp,
                    color = Color.Transparent
                ) else Modifier
            )
    ) {
        content()
    }
}

@Composable
private fun FactEyebrow(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun SpecGrid(specs: List<CoinUiModel.SpecEntry>) {
    // 2-column grid manually with Rows
    specs.chunked(2).forEach { pair ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            pair.forEach { (label, value) ->
                Column(modifier = Modifier.weight(1f)) {
                    FieldRow(label = label, value = value)
                }
            }
            if (pair.size == 1) Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun CatalogueSection(
    chips: List<String>,
    numistaUrl: String,
) {
    val uriHandler = LocalUriHandler.current
    SectionContainer {
        Eyebrow("Catalogue")
        if (chips.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                chips.forEach { label ->
                    SuggestionChip(
                        onClick = {},
                        label = { Text(text = label) },
                    )
                }
            }
        }
        if (numistaUrl.isNotEmpty()) {
            TextButton(
                onClick = { uriHandler.openUri(numistaUrl) },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "View on Numista ↗",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun ConditionSection(
    positives: List<String>,
    negatives: List<String>,
) {
    SectionContainer {
        Eyebrow("Condition")
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (positives.isNotEmpty()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "STRENGTHS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = AppThemeExt.colors.textSecondary,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    positives.forEach { feat ->
                        ConditionItem(text = feat, isPositive = true)
                    }
                }
            }
            if (negatives.isNotEmpty()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "WEAR",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = AppThemeExt.colors.textSecondary,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    negatives.forEach { feat ->
                        ConditionItem(text = feat, isPositive = false)
                    }
                }
            }
        }
    }
}

@Composable
private fun ConditionItem(text: String, isPositive: Boolean) {
    val (bg, fg, symbol) = if (isPositive) {
        Triple(
            AppThemeExt.statusColors.verifiedContainer,
            AppThemeExt.statusColors.verified,
            "+"
        )
    } else {
        Triple(
            AppThemeExt.statusColors.notFoundContainer,
            AppThemeExt.statusColors.notFound,
            "−"
        )
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 9.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(bg)
        ) {
            Text(
                text = symbol,
                color = fg,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ContextSection(
    contextBody: String,
    tags: List<String>,
    valueDisclaimer: String,
) {
    SectionContainer {
        Eyebrow("Context")

        if (contextBody.isNotEmpty()) {
            BodyText(contextBody)
        }

        if (tags.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tags.forEach { tag ->
                    SuggestionChip(
                        onClick = {},
                        label = { Text(text = tag) },
                    )
                }
            }
        }

        if (valueDisclaimer.isNotEmpty()) {
            Text(
                text = valueDisclaimer,
                style = MaterialTheme.typography.bodySmall,
                color = AppThemeExt.colors.textSecondary,
                fontStyle = FontStyle.Italic,
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun ActionButtons(
    onEditNotes: () -> Unit,
    onAddToSet: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        SecondaryButton(
            isLoading = false,
            text = "Edit notes",
            onButtonClick = { onEditNotes() },
            modifier = Modifier.weight(1f)
        )
        // Filled button
        PrimaryButton(
            isLoading = false,
            text = "Add to set",
            onButtonClick = { onAddToSet() },
            modifier = Modifier.weight(1f)
        )
    }
}


/** Wrapper with consistent horizontal padding for each section */
@Composable
private fun SectionContainer(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp)
    ) {
        content()
    }
}

/** Copper all-caps eyebrow label */
@Composable
private fun Eyebrow(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

/** Label + value pair */
@Composable
private fun FieldRow(label: String, value: String) {
    Text(
        text = label.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = AppThemeExt.colors.textSecondary,
    )
    Text(
        text = value,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(top = 2.dp)
    )
}

/** Body paragraph text */
@Composable
private fun BodyText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        lineHeight = 22.sp,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

/** Left-bordered inscription block */
@Composable
private fun InscriptionBlock(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(40.dp)
                .background(MaterialTheme.colorScheme.tertiary)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = AppThemeExt.colors.silver,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(start = 14.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
                .fillMaxWidth()
        )
    }
}

/** Designer attribution */
@Composable
private fun DesignerLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = AppThemeExt.colors.textSecondary,
    )
}

@Preview
@Composable
private fun CoinScreenPreview() {
    AppTheme {
        CoinScreen(
            state = CoinState(coin = dummyCoinUi),
            snackbarHostState = remember { SnackbarHostState() },
            onScreenAction = {}
        )
    }
}