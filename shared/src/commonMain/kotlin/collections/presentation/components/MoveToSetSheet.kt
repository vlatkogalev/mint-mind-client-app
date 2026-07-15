package collections.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.presentation.theme.AppTheme
import collections.domain.model.CoinSet
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.collection_create_set
import mintmind.shared.generated.resources.collection_empty_set_desc
import mintmind.shared.generated.resources.collection_move_to
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoveToSetSheet(
    sets: List<CoinSet>,
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.collection_move_to),
    onSelectSet: (setId: String) -> Unit,
    onCreateNewSet: () -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        if (sets.isEmpty()) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = stringResource(Res.string.collection_empty_set_desc))
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = {
                    onDismiss()
                    onCreateNewSet()
                }) {
                    Text(text = stringResource(Res.string.collection_create_set))
                }
            }
        } else {
            LazyColumn {
                items(sets, key = { it.id }) { set ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectSet(set.id) }
                            .padding(horizontal = 24.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = set.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "${set.coinCount}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview
@Composable
private fun MoveToSetSheetPopulatedPreview() {
    AppTheme {
        MoveToSetSheet(
            sets = listOf(
                CoinSet(
                    id = "1",
                    name = "My Collection",
                    description = null,
                    previewObverseUrls = emptyList(),
                    coinCount = 5,
                    totalValue = 100.0,
                    issuerCount = 3,
                    createdAt = 0L,
                    updatedAt = 0L
                ),
                CoinSet(
                    id = "2",
                    name = "Rare Coins",
                    description = null,
                    previewObverseUrls = emptyList(),
                    coinCount = 12,
                    totalValue = 500.0,
                    issuerCount = 5,
                    createdAt = 0L,
                    updatedAt = 0L
                ),
            ),
            onSelectSet = {},
            onCreateNewSet = {},
            onDismiss = {},
        )
    }
}

@Preview
@Composable
private fun MoveToSetSheetEmptyPreview() {
    AppTheme {
        MoveToSetSheet(
            sets = emptyList(),
            onSelectSet = {},
            onCreateNewSet = {},
            onDismiss = {},
        )
    }
}
