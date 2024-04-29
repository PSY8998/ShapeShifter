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

    val Grid = 4.dp

    val Hairline = 1.dp

    object Spacing {
        val Smallest: Dp = Grid.div(0.5f)

        val ExtraSmall: Dp = Grid

        val Small: Dp = Grid.times(2)

        val Medium: Dp = Grid.times(4)

        val Large: Dp = Grid.times(6)

        val ExtraLarge: Dp = Grid.times(8)

        val Largest: Dp = Grid.times(16)
    }
}
