package app.shapeshifter.feature.workout.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.shapeshifter.Clock
import app.shapeshifter.common.ui.compose.resources.Dimens
import app.shapeshifter.data.models.ExerciseTemplate
import app.shapeshifter.data.models.workout.Exercise
import app.shapeshifter.data.models.workout.ExerciseSession
import app.shapeshifter.feature.workout.ui.drawable.MoreHorizontal
import coil3.compose.AsyncImage

@Composable
fun Exercise(
    exerciseSession: ExerciseSession,
    isInEditMode: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        ExerciseHeader(
            exercise = exerciseSession.exercise,
            exerciseTemplate = exerciseSession.exerciseTemplate,
            onShowExerciseActions = {},
            onSetRestTimer = {},
        )
    }
}

@Composable
private fun ExerciseHeader(
    exercise: Exercise,
    exerciseTemplate: ExerciseTemplate,
    onShowExerciseActions: () -> Unit,
    onSetRestTimer: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Padding.Medium),
    ) {
        AsyncImage(
            model = exerciseTemplate.imageUrl,
            contentDescription = "${exerciseTemplate.name} image",
        )

        Column(
            modifier = Modifier
                .weight(1f, fill = true),
        ) {
            Text(
                text = exerciseTemplate.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            val noteTextFieldState = rememberTextFieldState(
                initialText = exercise.note ?: "",
            )

            BasicTextField(
                state = noteTextFieldState,
                textStyle = MaterialTheme.typography.bodySmall
                    .copy(
                        color = Color.Gray,
                    ),
                cursorBrush = SolidColor(Color.Gray),
                decorator = { innerTextField ->
                    if (noteTextFieldState.text.isEmpty()) {
                        Text(
                            text = "Go slow and control each movement",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                        )
                    }

                    innerTextField()
                },
                lineLimits = TextFieldLineLimits.MultiLine(
                    minHeightInLines = 1,
                    maxHeightInLines = 3,
                ),
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }

        IconButton(
            onClick = { onSetRestTimer() },
        ) {
            Icon(
                imageVector = Clock,
                tint = Color.Unspecified,
                contentDescription = "Rest Time Duration",
                modifier = Modifier
                    .size(24.dp),
            )
        }

        IconButton(
            onClick = { onShowExerciseActions() },
            modifier = Modifier,
        ) {
            Icon(
                imageVector = MoreHorizontal,
                contentDescription = "Options",
            )
        }
    }
}
