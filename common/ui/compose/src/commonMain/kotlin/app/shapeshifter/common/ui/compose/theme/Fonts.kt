package app.shapeshifter.common.ui.compose.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
expect fun font(
    fontName: String,
    resourceId: String,
    weight: FontWeight,
    style: FontStyle = FontStyle.Normal,
): Font

val PoppinsFontFamily: FontFamily
    @Composable get() = FontFamily(
        font(fontName = "Poppins", resourceId = "poppins_regular", weight = FontWeight.Normal),
        font(fontName = "Poppins", resourceId = "poppins_medium", weight = FontWeight.Medium),
        font(fontName = "Poppins", resourceId = "poppins_bold", weight = FontWeight.Bold),
    )
