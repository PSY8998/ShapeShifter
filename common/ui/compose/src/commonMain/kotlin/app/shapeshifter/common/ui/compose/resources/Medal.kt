package app.shapeshifter.common.ui.compose.resources

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val Medal: ImageVector
    get() {
        if (_Medal != null) {
            return _Medal!!
        }
        _Medal = ImageVector.Builder(
            name = "Medal",
            defaultWidth = 800.dp,
            defaultHeight = 800.dp,
            viewportWidth = 496.16f,
            viewportHeight = 496.16f
        ).apply {
            path(fill = SolidColor(Color(0xFF32BEA6))) {
                moveTo(0f, 248.09f)
                curveTo(0f, 111.06f, 111.07f, 0f, 248.08f, 0f)
                curveToRelative(137.01f, 0f, 248.08f, 111.06f, 248.08f, 248.08f)
                curveToRelative(0f, 137f, -111.07f, 248.07f, -248.08f, 248.07f)
                curveTo(111.07f, 496.15f, 0f, 385.09f, 0f, 248.09f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(340.06f, 262.06f)
                curveToRelative(0f, 16.48f, -13.36f, 29.83f, -29.83f, 29.83f)
                horizontalLineTo(185.93f)
                curveToRelative(-16.48f, 0f, -29.83f, -13.35f, -29.83f, -29.83f)
                verticalLineTo(137.76f)
                curveToRelative(0f, -16.47f, 13.35f, -29.83f, 29.83f, -29.83f)
                horizontalLineToRelative(124.3f)
                curveToRelative(16.48f, 0f, 29.83f, 13.36f, 29.83f, 29.83f)
                verticalLineToRelative(124.3f)
                horizontalLineTo(340.06f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(269.17f, 308.9f)
                curveToRelative(-11.65f, 11.65f, -30.54f, 11.65f, -42.19f, 0f)
                lineToRelative(-87.89f, -87.89f)
                curveToRelative(-11.65f, -11.65f, -11.65f, -30.54f, 0f, -42.2f)
                lineToRelative(87.89f, -87.89f)
                curveToRelative(11.65f, -11.65f, 30.54f, -11.65f, 42.19f, 0f)
                lineToRelative(87.9f, 87.89f)
                curveToRelative(11.65f, 11.65f, 11.65f, 30.54f, 0f, 42.2f)
                lineTo(269.17f, 308.9f)
                close()
            }
            path(fill = SolidColor(Color(0xFF32BEA6))) {
                moveTo(248.08f, 199.91f)
                moveToRelative(-82.04f, 0f)
                arcToRelative(82.04f, 82.04f, 0f, isMoreThanHalf = true, isPositiveArc = true, 164.08f, 0f)
                arcToRelative(82.04f, 82.04f, 0f, isMoreThanHalf = true, isPositiveArc = true, -164.08f, 0f)
            }
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(248.08f, 199.91f)
                moveToRelative(-71.38f, 0f)
                arcToRelative(71.38f, 71.38f, 0f, isMoreThanHalf = true, isPositiveArc = true, 142.76f, 0f)
                arcToRelative(71.38f, 71.38f, 0f, isMoreThanHalf = true, isPositiveArc = true, -142.76f, 0f)
            }
            path(fill = SolidColor(Color(0xFF32BEA6))) {
                moveTo(248.07f, 142.28f)
                lineToRelative(16.8f, 34.02f)
                lineToRelative(37.55f, 5.46f)
                lineToRelative(-27.18f, 26.48f)
                lineToRelative(6.42f, 37.4f)
                lineToRelative(-33.59f, -17.66f)
                lineToRelative(-33.59f, 17.66f)
                lineToRelative(6.42f, -37.4f)
                lineToRelative(-27.17f, -26.48f)
                lineToRelative(37.55f, -5.46f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(215.58f, 408.72f)
                lineToRelative(-65.67f, -23.33f)
                lineToRelative(62f, -99.83f)
                lineToRelative(34.67f, 0f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(280.58f, 408.72f)
                lineToRelative(65.67f, -23.33f)
                lineToRelative(-62f, -99.83f)
                lineToRelative(-34.67f, 0f)
                close()
            }
        }.build()

        return _Medal!!
    }

@Suppress("ObjectPropertyName")
private var _Medal: ImageVector? = null
