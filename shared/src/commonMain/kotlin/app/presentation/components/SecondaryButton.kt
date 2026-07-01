package app.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.presentation.theme.AppTheme

@Composable
fun SecondaryButton(
    isLoading: Boolean = false,
    text: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        content = {
            if (isLoading) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
            )
        },
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
        ),
        border = ButtonDefaults.outlinedButtonBorder(),
        enabled = !isLoading,
        onClick = { onButtonClick() },
        modifier = modifier
    )
}

@Preview
@Composable
private fun SecondaryButtonPreview() {
    AppTheme {
        SecondaryButton(
            isLoading = false,
            text = "Submit",
            onButtonClick = {}
        )
    }
}