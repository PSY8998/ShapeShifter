package app.shapeshifter.feature.workout.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat.Style
import app.shapeshifter.common.ui.compose.theme.Procelain
import com.slack.circuit.retained.rememberRetained

@Composable
fun WorkoutExercise(
    workoutExercise: WorkoutExercise,
    modifier: Modifier = Modifier,
) {
    var exerciseNote by rememberRetained(key = "exerciseNote") { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = workoutExercise.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = exerciseNote,
                onValueChange = {
                    exerciseNote = it
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Procelain,
                    unfocusedContainerColor = Color.Procelain,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                placeholder = {
                    Text(
                        text = "Drop Weight and focus on form",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Gray,
                    )
                },
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Set",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f),
            )
            Text(
                text = "Prev",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f),
            )
            Text(
                text = "Kg",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f),
            )
            Text(
                text = "Reps",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f),
            )
        }
        for (workoutSet in workoutExercise.sets) {
            WorkoutSet(
                workoutSet = workoutSet,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun WorkoutSet(
    workoutSet: WorkoutExerciseSet,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = workoutSet.index.toString(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .weight(1f),
        )
        Text(
            text = workoutSet.previousSet.weight.toString() + " x " + workoutSet.previousSet.reps.toString(),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f),

        )

        var setWeight by remember { mutableStateOf(workoutSet.weight.toString()) }

        BasicTextField(
            value = setWeight,
            onValueChange = {
                setWeight = it
            },
            modifier = Modifier
                .defaultMinSize(24.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
                .weight(1f)
                .width(IntrinsicSize.Min),
        )

        var setReps by remember { mutableStateOf(workoutSet.reps.toString()) }

        BasicTextField(
            value = setReps,
            onValueChange = {
                setReps = it
            },
            modifier = Modifier
                .defaultMinSize(24.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
                .weight(1f)
                .width(IntrinsicSize.Min),
        )
    }
}
