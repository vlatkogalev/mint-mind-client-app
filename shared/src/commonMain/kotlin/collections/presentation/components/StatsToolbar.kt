package collections.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.presentation.components.ReededDivider
import app.presentation.components.ReededDividerOrientation
import app.presentation.theme.AppTheme
import app.presentation.theme.AppThemeExt
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.collection_coins
import mintmind.shared.generated.resources.collection_issuers
import org.jetbrains.compose.resources.stringResource

@Composable
fun StatsToolbar(
    title: String,
    totalValue: Double,
    objectCount: Int,
    issuerCount: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "$$totalValue",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Light,
            color = AppThemeExt.colors.textSecondary,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.height(44.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            StatsToolbarItem(
                label = stringResource(Res.string.collection_coins),
                value = objectCount,
                modifier = Modifier.padding(vertical = 2.dp)
            )

            ReededDivider(
                orientation = ReededDividerOrientation.Vertical,
                gapLength = 4.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            StatsToolbarItem(
                label = stringResource(Res.string.collection_issuers),
                value = issuerCount,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun StatsToolbarItem(
    label: String,
    value: Int,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Light,
            color = AppThemeExt.colors.textSecondary
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
                .padding(vertical = 8.dp)
        ) {
            StatsToolbar(
                title = "Collection",
                totalValue = 1000.0,
                objectCount = 50,
                issuerCount = 10,
            )
        }
    }
}