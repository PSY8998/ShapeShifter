package app.shapeshifter.common.ui.compose.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val VampireBlack = Color(0xFF070707)
val Coal = Color(0xFF282828)
val Color.Companion.Procelain
    get() = Color(0xFFF2F5F6)


val DarkBackground = Color(0xFF101829)
val DarkSecondary = Color(0xFF202839)
val DarkPrimary = Color(0xFFF3F5F7)

val ShapeShifterLightColorScheme = lightColorScheme(
    primary = VampireBlack,
    secondary = Color.Procelain,
    onPrimary = Color.White,
    onSecondary = VampireBlack,
    surface = VampireBlack,
    onSurface = DarkPrimary,
    background = Color.White,
    onPrimaryContainer = VampireBlack,
    onSecondaryContainer = Color.Procelain,
    outlineVariant = VampireBlack.copy(alpha = 0.08f),
    outline = Color.Gray,
)

val ShapeShifterDarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    onPrimary = VampireBlack,
    onSecondary = Color.White,
    surface = DarkSecondary,
    background = DarkBackground,
    onSurface = DarkPrimary,
    onBackground = DarkPrimary,
    onPrimaryContainer = DarkPrimary,
    onSecondaryContainer = DarkSecondary,
    outlineVariant = DarkPrimary.copy(alpha = 0.08f),
    outline = Color.Gray,
)
