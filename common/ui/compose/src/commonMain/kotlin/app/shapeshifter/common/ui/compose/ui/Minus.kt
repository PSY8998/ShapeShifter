package app.shapeshifter.common.ui.compose.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

fun PaddingValues.minus(
    other: PaddingValues,
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
): PaddingValues = PaddingValues(
    start = (
        calculateStartPadding(layoutDirection) -
            other.calculateStartPadding(layoutDirection)
        ).coerceAtLeast(0.dp),
    top = (
        calculateTopPadding() -
            other.calculateTopPadding()
        ).coerceAtLeast(0.dp),
    end = (
        calculateEndPadding(layoutDirection) -
            other.calculateEndPadding(layoutDirection)
        ).coerceAtLeast(0.dp),
    bottom = (
        calculateBottomPadding() -
            other.calculateBottomPadding()
        ).coerceAtLeast(0.dp),
)
