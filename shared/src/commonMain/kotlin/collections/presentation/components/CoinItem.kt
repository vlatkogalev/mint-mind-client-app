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
import androidx.compose.foundation.layout.width
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
import collections.domain.model.Coin
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.coin_placeholder
import mintmind.shared.generated.resources.identify_coin_side_obverse
import mintmind.shared.generated.resources.identify_coin_side_reverse
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CoinItem(
    coin: Coin,
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
                .clickable { onClick(coin.id) }
                .padding(8.dp)
        ) {
            ObjectVisuals(
                obverseImagePath = coin.obverseThumbnailUrl ?: coin.obverseUrl,
                reverseImagePath = coin.reverseThumbnailUrl ?: coin.reverseUrl,
                modifier = Modifier.weight(0.35f)
            )

            Column(
                modifier = Modifier.fillMaxWidth().weight(0.65f)
            ) {
                Text(
                    text = coin.denomination,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "${coin.countryOrIssuer}${coin.year?.let { ", $it" } ?: ""}",
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = coin.estimatedValueMean?.let { "$$it" } ?: "\u2014",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "${coin.gradeName} (${coin.gradeAbbreviation}-${coin.gradeNumeric})",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ObjectVisuals(
    obverseImagePath: String,
    reverseImagePath: String,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxHeight()
    ) {
        AsyncImage(
            model = obverseImagePath,
            contentDescription = stringResource(Res.string.identify_coin_side_obverse),
            contentScale = ContentScale.FillBounds,
            placeholder = painterResource(Res.drawable.coin_placeholder),
            error = painterResource(Res.drawable.coin_placeholder),
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(CircleShape)
                .weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        AsyncImage(
            model = reverseImagePath,
            contentDescription = stringResource(Res.string.identify_coin_side_reverse),
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

@Preview
@Composable
private fun ObjectItemPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            CoinItem(
                coin = Coin.dummyItem,
                modifier = Modifier,
                onClick = { }
            )
        }
    }
}