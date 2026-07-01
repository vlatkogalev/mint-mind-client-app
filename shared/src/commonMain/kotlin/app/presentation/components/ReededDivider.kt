package app.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.presentation.theme.AppTheme

enum class ReededDividerOrientation {
    Horizontal,
    Vertical
}

@Composable
fun ReededDivider(
    modifier: Modifier = Modifier,
    orientation: ReededDividerOrientation = ReededDividerOrientation.Horizontal,
    color: Color = MaterialTheme.colorScheme.outline,
    dotRadius: Dp = 1.dp,
    gapLength: Dp = 6.dp,
) {
    Canvas(
        modifier = modifier.then(
            if (orientation == ReededDividerOrientation.Horizontal) {
                Modifier
                    .fillMaxWidth()
                    .height(dotRadius * 2)
            } else {
                Modifier
                    .width(dotRadius * 2)
                    .fillMaxHeight()
            }
        )
    ) {
        val strokeWidth = dotRadius.toPx() * 2

        drawLine(
            color = color,
            start = if (orientation == ReededDividerOrientation.Horizontal)
                Offset(0f, size.height / 2)
            else
                Offset(size.width / 2, 0f),
            end = if (orientation == ReededDividerOrientation.Horizontal)
                Offset(size.width, size.height / 2)
            else
                Offset(size.width / 2, size.height),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(
                    0f,
                    strokeWidth + gapLength.toPx()
                )
            )
        )
    }
}

@Preview
@Composable
private fun ReededDividerPreview() {
    AppTheme {
        ReededDivider(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)
        )
    }
}