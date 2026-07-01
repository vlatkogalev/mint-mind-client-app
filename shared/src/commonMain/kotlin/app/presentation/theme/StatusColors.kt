package app.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class StatusColors(
    val verified: Color,
    val verifiedContainer: Color,
    val pending: Color,
    val pendingContainer: Color,
    val needsReview: Color,
    val needsReviewContainer: Color,
    val notFound: Color,
    val notFoundContainer: Color,
)

val LocalStatusColors = staticCompositionLocalOf {
    StatusColors(
        verified = Color(0xFF97B799),
        verifiedContainer = Color(0xFF313730),
        pending = Color(0xFF9DB9C4),
        pendingContainer = Color(0xFF5D6B70),
        needsReview = Color(0xFFDFAB6A),
        needsReviewContainer = Color(0xFF63513A),
        notFound = Color(0xFFD98A75),
        notFoundContainer = Color(0xFF3D2D28),
    )
}