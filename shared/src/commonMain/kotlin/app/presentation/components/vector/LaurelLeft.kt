package app.presentation.components.vector

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val LaurelLeft: ImageVector
    get() {
        if (_LaurelLeft != null) {
            return _LaurelLeft!!
        }
        _LaurelLeft = ImageVector.Builder(
            name = "LaurelLeft",
            defaultWidth = 120.dp,
            defaultHeight = 200.dp,
            viewportWidth = 415f,
            viewportHeight = 692f
        ).apply {
            path(fill = SolidColor(Color(0xFF2A2D26))) {
                moveToRelative(301.5f, 579.9f)
                quadToRelative(4.9f, -5.7f, 9f, -12.1f)
                curveToRelative(13.3f, -20.8f, 19.7f, -47.2f, 15.4f, -72.4f)
                curveToRelative(-4.3f, -25.1f, -19.8f, -48.4f, -41.4f, -59.1f)
                curveToRelative(-23f, 40.6f, -35.9f, 86f, -1.9f, 131.4f)
                curveToRelative(-14.6f, -9.4f, -29f, -18.9f, -42.6f, -29.1f)
                quadToRelative(-10.4f, -7.7f, -20.3f, -16.1f)
                quadToRelative(5f, -5.4f, 9.2f, -11.5f)
                curveToRelative(13.2f, -19.1f, 20.1f, -43.7f, 16.9f, -67.4f)
                curveToRelative(-3.2f, -23.7f, -17f, -46.1f, -36.9f, -56.9f)
                curveToRelative(-21.6f, 35.1f, -34.9f, 74.6f, -11.1f, 115.5f)
                quadToRelative(-6.6f, -6.5f, -12.9f, -13.5f)
                curveToRelative(-17.5f, -18.8f, -32.7f, -39.8f, -45.2f, -62.1f)
                quadToRelative(6.9f, -3.7f, 13.2f, -8.5f)
                curveToRelative(18.5f, -14f, 32.7f, -35.3f, 37.1f, -58.8f)
                curveToRelative(4.3f, -23.5f, -1.7f, -49.1f, -17.3f, -65.6f)
                curveToRelative(-31.1f, 26.4f, -55.8f, 59.2f, -47f, 104.6f)
                curveToRelative(-12.9f, -29.6f, -21.3f, -61.1f, -24.3f, -93f)
                quadToRelative(7.8f, -0.8f, 15.5f, -2.9f)
                curveToRelative(22.4f, -6.1f, 43.5f, -20.5f, 56.3f, -40.7f)
                curveToRelative(12.9f, -20.2f, 16.8f, -46.1f, 8.6f, -67.3f)
                curveToRelative(-37f, 12.3f, -70.8f, 32.2f, -81.4f, 73.6f)
                curveToRelative(1.3f, -32.7f, 8.6f, -65.4f, 22.7f, -96.4f)
                quadToRelative(6.7f, 2.2f, 13.7f, 3.6f)
                curveToRelative(22.8f, 4.4f, 48.1f, 0.7f, 68.5f, -11.8f)
                curveToRelative(20.4f, -12.4f, 35.4f, -34f, 37.2f, -56.6f)
                curveToRelative(-33.6f, -4.6f, -67.7f, -2.9f, -94.3f, 19.2f)
                quadToRelative(0.7f, -1f, 1.3f, -2.1f)
                curveToRelative(6.3f, -9.7f, 12.8f, -19.3f, 19.7f, -28.6f)
                curveToRelative(71.2f, -1.1f, 100.8f, -64.9f, 95.4f, -94f)
                curveToRelative(-23.3f, -4.5f, -43.3f, 2f, -62.8f, 15.8f)
                curveToRelative(-19.6f, 13.8f, -33f, 35.6f, -38f, 58.3f)
                quadToRelative(-1.1f, 5f, -1.7f, 10.1f)
                quadToRelative(-4.1f, 5.3f, -8.1f, 10.8f)
                curveToRelative(-12.6f, 17.4f, -24.2f, 35.4f, -34.4f, 54.3f)
                curveToRelative(5.6f, -42.4f, -18.3f, -73.4f, -47.9f, -98.6f)
                curveToRelative(-15.6f, 16.5f, -21.7f, 42.1f, -17.3f, 65.6f)
                curveToRelative(4.3f, 23.5f, 18.5f, 44.8f, 36.9f, 58.8f)
                quadToRelative(6f, 4.6f, 12.6f, 8.2f)
                curveToRelative(-10.6f, 28f, -16.3f, 57.6f, -17.4f, 87.5f)
                curveToRelative(-16.2f, -33.7f, -49f, -48.5f, -83.9f, -56.6f)
                curveToRelative(-6.3f, 21.8f, 0f, 47.3f, 14.6f, 66.2f)
                curveToRelative(14.7f, 19f, 36.9f, 31.4f, 59.8f, 35.4f)
                quadToRelative(5.7f, 1f, 11.4f, 1.4f)
                curveToRelative(3.5f, 29.8f, 11.4f, 59.1f, 23.3f, 86.5f)
                curveToRelative(-31.2f, -32.5f, -71.6f, -31.9f, -110.7f, -22.7f)
                curveToRelative(4.1f, 22.4f, 21f, 42.4f, 42.6f, 52.8f)
                curveToRelative(21.5f, 10.5f, 47f, 11.7f, 69.3f, 5.1f)
                quadToRelative(7f, -2.1f, 13.6f, -5.1f)
                curveToRelative(14.9f, 26.9f, 33.4f, 51.7f, 54.5f, 74.1f)
                curveToRelative(-38.1f, -17.8f, -74.7f, -4.9f, -107.5f, 15.3f)
                curveToRelative(10.8f, 19.9f, 33.1f, 33.7f, 56.9f, 36.8f)
                curveToRelative(23.7f, 3.2f, 48.3f, -3.6f, 67.4f, -16.8f)
                quadToRelative(5.8f, -4f, 11f, -8.7f)
                quadToRelative(10.1f, 8.7f, 20.7f, 16.8f)
                curveToRelative(14.2f, 10.8f, 29.1f, 20.9f, 44f, 30.8f)
                curveToRelative(-50.5f, -21.1f, -91.1f, 1.2f, -125.1f, 31.9f)
                curveToRelative(15.3f, 18.8f, 41.4f, 28.7f, 66.8f, 27.4f)
                curveToRelative(25.5f, -1.3f, 49.9f, -13.3f, 67.3f, -30.8f)
                curveToRelative(4.4f, -4.6f, 8.5f, -9.4f, 12f, -14.6f)
                curveToRelative(38.8f, 25.7f, 78.2f, 53f, 104f, 92f)
                curveToRelative(1.8f, 2.8f, 5.6f, 3.6f, 8.4f, 1.9f)
                lineToRelative(3f, -1.9f)
                curveToRelative(5f, -3.1f, 6.3f, -9.7f, 2.9f, -14.5f)
                curveToRelative(-28.6f, -40.5f, -69.8f, -68.2f, -111.3f, -94.9f)
                close()
            }
        }.build()

        return _LaurelLeft!!
    }

@Suppress("ObjectPropertyName")
private var _LaurelLeft: ImageVector? = null
