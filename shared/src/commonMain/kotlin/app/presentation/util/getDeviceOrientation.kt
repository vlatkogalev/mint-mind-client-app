package app.presentation.util

import androidx.window.core.layout.WindowSizeClass

enum class DeviceOrientation {
    PORTRAIT, LANDSCAPE
}

fun WindowSizeClass.getDeviceOrientation(): DeviceOrientation {
    return if (minWidthDp >= minHeightDp) {
        DeviceOrientation.LANDSCAPE
    } else {
        DeviceOrientation.PORTRAIT
    }
}