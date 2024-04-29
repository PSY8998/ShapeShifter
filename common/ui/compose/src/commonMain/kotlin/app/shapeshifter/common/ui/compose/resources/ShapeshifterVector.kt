@file:Suppress("ObjectPropertyName")

package app.shapeshifter.common.ui.compose.resources

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private var _ShapeshifterDark: ImageVector? = null
private var _ShapeshifterLight: ImageVector? = null

fun shapeshifter(dark: Boolean): ImageVector {
    if (dark && _ShapeshifterDark != null) {
        return _ShapeshifterDark as ImageVector
    }

    if (!dark && _ShapeshifterLight != null) {
        return _ShapeshifterLight as ImageVector
    }

    return if (dark) {
        _ShapeshifterDark = buildShapeShifterVector(Color.Black)
        _ShapeshifterDark!!
    } else {
        _ShapeshifterLight = buildShapeShifterVector(Color.White)
        _ShapeshifterLight!!
    }
}

fun buildShapeShifterVector(
    tint: Color,
): ImageVector {
    return ImageVector.Builder(
        name = "Shapeshifter2",
        defaultWidth = 18.0.dp,
        defaultHeight = 18.0.dp,
        viewportWidth = 24.0f,
        viewportHeight = 23.0f,
    ).apply {
        path(
            fill = SolidColor(tint),
            stroke = SolidColor(tint),
            strokeLineWidth = 0.0f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 4.0f,
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(11.0703f, 22.5938f)
            curveTo(7.4414f, 17.6953f, 3.8125f, 12.7969f, 0.1836f, 7.8984f)
            curveTo(0.3008f, 7.793f, 0.4023f, 7.8281f, 0.4922f, 7.8281f)
            curveTo(1.6875f, 7.8242f, 2.8828f, 7.832f, 4.0742f, 7.8203f)
            curveTo(4.3359f, 7.8164f, 4.4961f, 7.8984f, 4.6484f, 8.1055f)
            curveTo(6.4883f, 10.6016f, 8.3398f, 13.0898f, 10.1875f, 15.582f)
            curveTo(10.5547f, 16.0781f, 10.918f, 16.5742f, 11.2891f, 17.0664f)
            curveTo(11.3828f, 17.1953f, 11.4375f, 17.3203f, 11.4375f, 17.4844f)
            curveTo(11.4336f, 19.2383f, 11.4336f, 20.9922f, 11.4336f, 22.7461f)
            curveTo(11.4336f, 22.8164f, 11.4531f, 22.8906f, 11.3789f, 22.9688f)
            curveTo(11.25f, 22.875f, 11.1797f, 22.7266f, 11.0703f, 22.5938f)
            moveTo(18.2305f, 9.2227f)
            curveTo(18.5352f, 8.8008f, 18.832f, 8.3984f, 19.1211f, 7.9883f)
            curveTo(19.1914f, 7.8867f, 19.2695f, 7.8242f, 19.4023f, 7.8242f)
            curveTo(20.7188f, 7.8281f, 22.0352f, 7.8281f, 23.3516f, 7.8281f)
            curveTo(23.3867f, 7.8281f, 23.4219f, 7.8398f, 23.5273f, 7.8594f)
            lineTo(12.4414f, 23.0f)
            curveTo(12.3242f, 22.8789f, 12.3555f, 22.7734f, 12.3555f, 22.6836f)
            curveTo(12.3711f, 20.9648f, 12.3867f, 19.25f, 12.3945f, 17.5313f)
            curveTo(12.3984f, 17.3438f, 12.4766f, 17.207f, 12.5781f, 17.0703f)
            curveTo(14.4141f, 14.5273f, 16.2461f, 11.9805f, 18.082f, 9.4375f)
            curveTo(18.1289f, 9.3711f, 18.1719f, 9.3047f, 18.2305f, 9.2227f)
            moveTo(13.1641f, 4.0664f)
            curveTo(11.1719f, 4.0664f, 9.2109f, 4.0586f, 7.2461f, 4.0703f)
            curveTo(6.9766f, 4.0703f, 6.8398f, 3.9844f, 6.7344f, 3.7266f)
            curveTo(6.2656f, 2.582f, 5.7695f, 1.4492f, 5.2852f, 0.3086f)
            curveTo(5.2461f, 0.2227f, 5.1836f, 0.1367f, 5.2188f, 0.0f)
            lineTo(18.8047f, 0.0f)
            curveTo(18.7734f, 0.2305f, 18.6719f, 0.4023f, 18.5938f, 0.5781f)
            curveTo(18.1289f, 1.6367f, 17.6523f, 2.6875f, 17.1953f, 3.75f)
            curveTo(17.0938f, 3.9844f, 16.9688f, 4.0703f, 16.7188f, 4.0703f)
            curveTo(15.543f, 4.0586f, 14.3672f, 4.0664f, 13.1641f, 4.0664f)
            moveTo(22.9805f, 6.8242f)
            curveTo(21.9805f, 6.8242f, 21.0078f, 6.8203f, 20.0352f, 6.8281f)
            curveTo(19.8477f, 6.8281f, 19.7227f, 6.7695f, 19.6094f, 6.6172f)
            curveTo(19.1094f, 5.918f, 18.6016f, 5.2266f, 18.0977f, 4.5352f)
            curveTo(18.0234f, 4.4336f, 17.9609f, 4.3398f, 18.0234f, 4.1914f)
            curveTo(18.5039f, 3.082f, 18.9766f, 1.9688f, 19.4492f, 0.8555f)
            curveTo(19.4727f, 0.8047f, 19.4961f, 0.7578f, 19.6016f, 0.7227f)
            lineTo(23.9883f, 6.7305f)
            lineTo(23.9648f, 6.8242f)
            close()
            moveTo(1.3594f, 4.8516f)
            curveTo(2.3398f, 3.4922f, 3.3242f, 2.1328f, 4.3086f, 0.7734f)
            curveTo(4.3203f, 0.7539f, 4.3516f, 0.7461f, 4.4102f, 0.7148f)
            curveTo(4.5859f, 1.1211f, 4.7578f, 1.5195f, 4.9297f, 1.918f)
            curveTo(5.2344f, 2.6367f, 5.5313f, 3.3594f, 5.8477f, 4.0703f)
            curveTo(5.9453f, 4.2891f, 5.918f, 4.4414f, 5.7773f, 4.6289f)
            curveTo(5.2852f, 5.2891f, 4.8086f, 5.9609f, 4.332f, 6.6289f)
            curveTo(4.2422f, 6.7578f, 4.1406f, 6.8242f, 3.9844f, 6.8242f)
            curveTo(2.7539f, 6.8203f, 1.5234f, 6.8203f, 0.2969f, 6.8164f)
            curveTo(0.2109f, 6.8164f, 0.1172f, 6.8281f, 0.0117f, 6.7344f)
            curveTo(0.457f, 6.1094f, 0.9023f, 5.4883f, 1.3594f, 4.8516f)
        }
    }
        .build()
}
