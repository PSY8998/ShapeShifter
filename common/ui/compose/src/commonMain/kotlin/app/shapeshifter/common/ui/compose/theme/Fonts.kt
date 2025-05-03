package app.shapeshifter.common.ui.compose.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import app.shapeshifter.common.ui.resources.Res
import app.shapeshifter.common.ui.resources.poppins_bold
import app.shapeshifter.common.ui.resources.poppins_medium
import app.shapeshifter.common.ui.resources.poppins_regular
import org.jetbrains.compose.resources.Font

val PoppinsFontFamily: FontFamily
    @Composable get() = FontFamily(
        Font(Res.font.poppins_regular, weight = FontWeight.Normal),
        Font(Res.font.poppins_medium, weight = FontWeight.Medium),
        Font(Res.font.poppins_bold, weight = FontWeight.Bold),
    )
