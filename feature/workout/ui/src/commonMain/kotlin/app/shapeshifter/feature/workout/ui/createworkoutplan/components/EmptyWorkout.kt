package app.shapeshifter.feature.workout.ui.createworkoutplan.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import shapeshifter.feature.workout.ui.generated.resources.Res
import shapeshifter.feature.workout.ui.generated.resources.ic_dumbbell_workout

@Composable
fun EmptyWorkout(
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_dumbbell_workout),
            contentDescription = "No workouts",
            modifier = Modifier
                .size(48.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        )

        val fontFamily = MaterialTheme.typography.titleMedium.fontFamily

        Text(
            text = buildAnnotatedString {
                append("BETTER")
                append("\n")
                withStyle(
                    SpanStyle(
                        fontSize = 34.sp,
                        fontFamily = fontFamily,
                    ),
                ) {
                    append("SORE")
                }
                append("\n")
                append("THEN")
                withStyle(
                    SpanStyle(
                        fontSize = 8.sp,
                    ),
                ) {
                    append("\n")
                }
                withStyle(
                    SpanStyle(
                        fontSize = 34.sp,
                    ),
                ) {
                    append("SORRY")
                }
            },
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
    }
}
