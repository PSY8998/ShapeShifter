package app.shapeshifter.common.ui.compose.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun ShapeShifterTheme(
    useDarkTheme: Boolean,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (useDarkTheme) {
            ShapeShifterDarkColorScheme
        } else {
            ShapeShifterLightColorScheme
        },
        typography = ShapeShifterTypography,
    ) {
        content()
    }
}
