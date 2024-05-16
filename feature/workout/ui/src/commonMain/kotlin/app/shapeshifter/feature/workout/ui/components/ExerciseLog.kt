package app.shapeshifter.feature.workout.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.resources.Dimens
import app.shapeshifter.data.models.PositiveInt
import app.shapeshifter.data.models.workoutlog.ExerciseSession
import app.shapeshifter.data.models.workoutlog.SetLog
import com.slack.circuit.retained.rememberRetained
import shapeshifter.feature.workout.ui.generated.resources.Res

@Composable
fun ExerciseLog(
    name: String,
    modifier: Modifier = Modifier,
) {
    var exerciseNote by rememberRetained(key = "exerciseNote") { mutableStateOf("") }

    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = name,
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
                .fillMaxWidth()
                .padding(vertical = Dimens.Spacing.Small),
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = exerciseNote,
                onValueChange = {
                    exerciseNote = it
                },
                textStyle = MaterialTheme.typography.bodySmall,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                maxLines = 2,
                placeholder = {
                    Text(
                        text = "Note: drop Weight and focus on form",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                    )
                },
            )
        }
    }
}

@Composable
fun SetColumnTitles(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.Spacing.Small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Set",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f),
        )
        Text(
            text = "Prev",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f),
        )
        Text(
            text = "Kg",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f),
        )
        Text(
            text = "Reps",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f),
        )
        Box(
            modifier = Modifier
                .weight(1f),
        )

    }
}

@Composable
fun SetLog(
    setLog: SetLog,
    onComplete: (
        set: SetLog,
    ) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isCompleted by remember(setLog.finishTime) {
        mutableStateOf(setLog.finishTime > 0)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isCompleted) {
                    Modifier.background(MaterialTheme.colorScheme.secondary)
                } else
                    Modifier,
            )
            .padding(vertical = Dimens.Spacing.Small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = setLog.index.value.toString(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f),
        )
        Text(
            text = "",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f),

            )

        var setWeight by remember { mutableStateOf(setLog.weight.toString()) }

        BasicTextField(
            value = setWeight,
            onValueChange = {
                if (pattern.matches(it)) {
                    setWeight = it
                }
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Black,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
            ),
            modifier = Modifier
                .wrapContentWidth(Alignment.CenterHorizontally)
                .weight(1f)
                .width(IntrinsicSize.Min)
                .defaultMinSize(24.dp),
        )

        var setReps by remember { mutableStateOf(setLog.reps.toString()) }

        BasicTextField(
            value = setReps,
            onValueChange = {
                if (pattern.matches(it)) {
                    setReps = it
                }
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Black,
            ),
            decorationBox = { internalField ->
                Box(
                    modifier = Modifier
                        .wrapContentWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    internalField()

                    if (setReps.isBlank()) {
                        Text(
                            text = "0",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Black,
                            ),
                        )
                    }
                }
            },
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
            ),
            modifier = Modifier
                .wrapContentWidth(Alignment.CenterHorizontally)
                .weight(1f)
                .width(IntrinsicSize.Min)
                .defaultMinSize(24.dp),
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(align = Alignment.CenterHorizontally)
                .background(
                    color = if (isCompleted) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary,
                    shape = MaterialTheme.shapes.small,
                )
                .clip(MaterialTheme.shapes.small)
                .toggleable(
                    value = isCompleted,
                    onValueChange = {
                        isCompleted = it
                        onComplete(
                            setLog.copy(
                                weight = PositiveInt(setWeight.toIntOrNull() ?: 0),
                                reps = PositiveInt(setReps.toIntOrNull() ?: 0),
                                finishTime = if (it) System.currentTimeMillis() else 0,
                            ),
                        )
                    },
                )
                .padding(Dimens.Spacing.ExtraSmall),
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Complete Set",
                tint = if (isCompleted) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

@Composable
fun AddNewSet(
    onAddSet: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(align = Alignment.CenterHorizontally)
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = MaterialTheme.shapes.small,
                )
                .clip(shape = MaterialTheme.shapes.small)
                .clickable {
                    onAddSet()
                }
                .padding(4.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
            )
        }

        Box(
            modifier = Modifier
                .weight(1f),
        )

        Box(
            modifier = Modifier
                .weight(1f),
        )

        Box(
            modifier = Modifier
                .weight(1f),
        )

        Box(
            modifier = Modifier
                .weight(1f),
        )
    }
}

val pattern = Regex("^\\d*$")
