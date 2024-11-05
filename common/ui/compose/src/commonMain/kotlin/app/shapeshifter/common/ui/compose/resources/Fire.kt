package app.shapeshifter.common.ui.compose.resources

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val Fire: ImageVector
    get() {
        if (_Fire != null) {
            return _Fire!!
        }
        _Fire = ImageVector.Builder(
            name = "Fire",
            defaultWidth = 800.dp,
            defaultHeight = 800.dp,
            viewportWidth = 255f,
            viewportHeight = 255f
        ).apply {
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFFFF4C0D),
                        1f to Color(0xFFFC9502)
                    ),
                    start = Offset(127.14f, 255f),
                    end = Offset(127.14f, 0.19f)
                ),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(220.9f, 164.81f)
                curveTo(218.8f, 214.87f, 177.57f, 254.81f, 127f, 254.81f)
                curveTo(75.08f, 254.81f, 33f, 211.31f, 33f, 160.81f)
                curveTo(33f, 154.06f, 32.88f, 140.57f, 43f, 117.81f)
                curveTo(49.06f, 104.19f, 52.86f, 95.63f, 55f, 87.81f)
                curveTo(56.18f, 83.51f, 58.47f, 76.68f, 65f, 87.81f)
                curveTo(68.85f, 94.37f, 69f, 103.81f, 69f, 103.81f)
                curveTo(69f, 103.81f, 83.33f, 92.82f, 93f, 71.81f)
                curveTo(107.18f, 41.02f, 95.87f, 22.61f, 92f, 9.81f)
                curveTo(90.66f, 5.38f, 89.82f, -2.57f, 99f, 0.81f)
                curveTo(108.35f, 4.26f, 133.08f, 21.57f, 146f, 39.81f)
                curveTo(164.45f, 65.85f, 171f, 90.81f, 171f, 90.81f)
                curveTo(171f, 90.81f, 176.91f, 83.48f, 179f, 75.81f)
                curveTo(181.37f, 67.15f, 181.4f, 58.57f, 189f, 67.81f)
                curveTo(196.23f, 76.6f, 206.96f, 93.11f, 213f, 108.81f)
                curveTo(223.97f, 137.32f, 220.9f, 164.81f, 220.9f, 164.81f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFC9502)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(127f, 254.81f)
                curveTo(91.1f, 254.81f, 62f, 225.71f, 62f, 189.81f)
                curveTo(62f, 168.15f, 70.73f, 155f, 88.9f, 137.17f)
                curveTo(100.53f, 125.75f, 111.42f, 111.72f, 116.04f, 102.17f)
                curveTo(116.95f, 100.29f, 119.03f, 90.5f, 127.02f, 101.97f)
                curveTo(131.21f, 107.98f, 137.79f, 118.68f, 142f, 127.81f)
                curveTo(149.27f, 143.55f, 151f, 158.81f, 151f, 158.81f)
                curveTo(151f, 158.81f, 158.12f, 154.62f, 163f, 143.81f)
                curveTo(164.57f, 140.33f, 167.75f, 127.15f, 176.64f, 140.33f)
                curveTo(183.17f, 150f, 192.13f, 167.39f, 192f, 189.81f)
                curveTo(192f, 225.71f, 162.9f, 254.81f, 127f, 254.81f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFCE202)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(128f, 183.81f)
                curveTo(137.25f, 183.81f, 137.25f, 200.94f, 149f, 223.81f)
                curveTo(156.82f, 239.04f, 145.12f, 254.81f, 128f, 254.81f)
                curveTo(110.88f, 254.81f, 102f, 240.93f, 102f, 223.81f)
                curveTo(102f, 206.69f, 118.75f, 183.81f, 128f, 183.81f)
                close()
            }
        }.build()

        return _Fire!!
    }

@Suppress("ObjectPropertyName")
private var _Fire: ImageVector? = null
