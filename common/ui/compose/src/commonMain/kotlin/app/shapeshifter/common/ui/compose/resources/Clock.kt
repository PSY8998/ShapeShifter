package app.shapeshifter

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val Clock: ImageVector
    get() {
        if (_Clock != null) {
            return _Clock!!
        }
        _Clock = ImageVector.Builder(
            name = "Clock",
            defaultWidth = 800.dp,
            defaultHeight = 800.dp,
            viewportWidth = 512f,
            viewportHeight = 512f
        ).apply {
            path(fill = SolidColor(Color(0xFF446080))) {
                moveTo(0f, 256f)
                curveToRelative(0f, 141.38f, 114.61f, 256f, 256f, 256f)
                lineToRelative(33.39f, -256f)
                lineTo(256f, 0f)
                curveTo(114.61f, 0f, 0f, 114.61f, 0f, 256f)
                close()
            }
            path(fill = SolidColor(Color(0xFF324860))) {
                moveTo(256f, 0f)
                verticalLineToRelative(512f)
                curveToRelative(141.38f, 0f, 256f, -114.62f, 256f, -256f)
                reflectiveCurveTo(397.39f, 0f, 256f, 0f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(66.78f, 256f)
                curveToRelative(0f, 104.5f, 84.72f, 189.22f, 189.22f, 189.22f)
                lineTo(289.39f, 256f)
                lineTo(256f, 66.78f)
                curveTo(151.5f, 66.78f, 66.78f, 151.5f, 66.78f, 256f)
                close()
            }
            path(fill = SolidColor(Color(0xFFBFDCFF))) {
                moveTo(256f, 66.78f)
                verticalLineToRelative(378.43f)
                curveToRelative(104.5f, 0f, 189.22f, -84.72f, 189.22f, -189.22f)
                reflectiveCurveTo(360.5f, 66.78f, 256f, 66.78f)
                close()
            }
            path(fill = SolidColor(Color(0xFF2D79CC))) {
                moveTo(389.57f, 272.7f)
                lineToRelative(-150.26f, 0f)
                lineToRelative(0f, -150.26f)
                lineToRelative(33.39f, 0f)
                lineToRelative(0f, 116.87f)
                lineToRelative(116.87f, 0f)
                close()
            }
        }.build()

        return _Clock!!
    }

@Suppress("ObjectPropertyName")
private var _Clock: ImageVector? = null
