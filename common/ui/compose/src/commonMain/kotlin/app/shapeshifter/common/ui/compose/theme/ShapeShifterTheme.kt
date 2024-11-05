package app.shapeshifter.common.ui.compose.theme

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

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
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyMedium) {
            content()
        }
    }
}
