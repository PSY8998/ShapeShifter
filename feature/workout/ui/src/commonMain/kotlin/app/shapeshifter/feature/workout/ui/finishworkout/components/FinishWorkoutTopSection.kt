package app.shapeshifter.feature.workout.ui.finishworkout.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import app.shapeshifter.common.ui.compose.resources.Dimens

@Composable
fun FinishWorkoutTopSection(
    workoutName: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        var planName by remember { mutableStateOf(workoutName) }

        TextField(
            modifier = Modifier
                .padding(top = Dimens.Padding.Small)
                .fillMaxWidth()
                .fillMaxWidth(fraction = 0.6f)
                .wrapContentWidth(align = Alignment.CenterHorizontally),
            value = planName,
            onValueChange = {
                planName = it
            },
            textStyle = MaterialTheme.typography.bodyMedium
                .copy(textAlign = TextAlign.Center),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            shape = MaterialTheme.shapes.small,
            maxLines = 2,
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Words,
            ),
            placeholder = {
                Text(
                    text = "Plan name",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                )
            },
        )
    }
}
