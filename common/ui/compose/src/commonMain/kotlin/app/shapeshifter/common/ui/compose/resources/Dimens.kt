package app.shapeshifter.common.ui.compose.resources

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Naming convention:
 *
 * Smallest
 * ExtraSmall
 * Small
 * SmallMedium
 * Medium
 * MediumLarge
 * Large
 * Largest
 */
object Dimens {

    val Hairline = 1.dp

    // define spacing as relation between components
    object Padding {
        val Smallest: Dp = 2.dp

        val ExtraSmall: Dp = 4.dp

        val Small: Dp = 8.dp

        val Medium: Dp = 16.dp

        val Large: Dp = 24.dp

        val ExtraLarge: Dp = 32.dp

        val Largest: Dp = 64.dp
    }
}
