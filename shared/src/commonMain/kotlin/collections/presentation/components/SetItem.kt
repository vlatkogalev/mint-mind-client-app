package collections.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.presentation.theme.AppTheme
import coil3.compose.AsyncImage
import collections.domain.model.CoinSet
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.coin_placeholder
import org.jetbrains.compose.resources.painterResource

private const val MAX_PREVIEWS = 3

@Composable
fun SetItem(
    set: CoinSet,
    modifier: Modifier = Modifier,
    onClick: (id: String) -> Unit,
) {
    Card(
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        modifier = modifier.height(132.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick(set.id) }
                .padding(8.dp)
        ) {
            SetPreviews(
                previewUrls = set.previewObverseUrls,
                modifier = Modifier.weight(1f)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxHeight().weight(1f)
            ) {
                Text(
                    text = set.name,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${set.coinCount}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SetPreviews(
    previewUrls: List<String>,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxHeight()
    ) {
        val previews = previewUrls.take(MAX_PREVIEWS)
        if (previews.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .weight(1f)
            ) {
                AsyncImage(
                    model = null,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    placeholder = painterResource(Res.drawable.coin_placeholder),
                    error = painterResource(Res.drawable.coin_placeholder),
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            previews.forEach { url ->
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    placeholder = painterResource(Res.drawable.coin_placeholder),
                    error = painterResource(Res.drawable.coin_placeholder),
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .weight(1f)
                )
            }
        }
    }
}

@Preview
@Composable
private fun SetItemPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            SetItem(
                set = CoinSet(
                    id = "1",
                    name = "Roman Empire",
                    description = "Ancient Roman coins",
                    previewObverseUrls = emptyList(),
                    coinCount = 12,
                    createdAt = 0L
                ),
                modifier = Modifier,
                onClick = { }
            )
        }
    }
}
