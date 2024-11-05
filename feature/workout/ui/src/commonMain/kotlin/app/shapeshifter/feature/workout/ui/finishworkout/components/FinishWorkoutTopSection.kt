package app.shapeshifter.feature.workout.ui.finishworkout.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun FinishWorkoutTopSection(
    workoutName: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .height(52.dp)
            .fillMaxWidth(),
    ) {
        val workoutNameTextFieldState = rememberTextFieldState(
            initialText = workoutName,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.2f),
            contentAlignment = Alignment.CenterStart,
        ) {
            IconButton(
                onClick = {

                },
                modifier = Modifier,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    modifier = Modifier,
                    contentDescription = "",
                )
            }
        }


        BasicTextField(
            state = workoutNameTextFieldState,
            textStyle = MaterialTheme.typography.titleMedium
                .copy(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                ),
            cursorBrush = SolidColor(
                MaterialTheme.colorScheme.onSurface,
            ),
            decorator = { innerTextField ->
                if (workoutNameTextFieldState.text.isEmpty()) {
                    Text(
                        text = "Enter plan name here",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                }

                innerTextField()
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(fraction = 0.6f)
                .wrapContentWidth(align = Alignment.CenterHorizontally),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.2f),
        )
    }
}
