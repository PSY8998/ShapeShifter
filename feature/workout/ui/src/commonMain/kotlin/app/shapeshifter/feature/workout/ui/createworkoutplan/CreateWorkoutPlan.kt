package app.shapeshifter.feature.workout.ui.createworkoutplan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.NestedScaffold
import app.shapeshifter.common.ui.compose.resources.Dimens
import app.shapeshifter.common.ui.compose.screens.CreateWorkoutPlanScreen
import app.shapeshifter.data.models.plans.ExercisePlanSession
import app.shapeshifter.data.models.plans.SetPlan
import app.shapeshifter.feature.workout.ui.components.pattern
import app.shapeshifter.feature.workout.ui.createworkoutplan.components.EmptyWorkout
import app.shapeshifter.feature.workout.ui.createworkoutplan.components.ExerciseTypeRadarChart
import coil3.compose.AsyncImage
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject

@Inject
class CreateWorkoutPlanUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is CreateWorkoutPlanScreen -> ui<CreateWorkoutPlanUiState> { state, modifier ->
                CreateWorkoutPlan(
                    uiState = state,
                    modifier = modifier,
                )
            }

            else -> null
        }
    }
}

@Composable
internal fun CreateWorkoutPlan(
    uiState: CreateWorkoutPlanUiState,
    modifier: Modifier = Modifier,
) {
    NestedScaffold(
        modifier = modifier
            .fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxSize(),
        ) {
            CreateWorkoutPlanTopBar(
                onSave = {
                    if (uiState.workoutPlanSession != null) {
                        uiState.eventSink(
                            CreateWorkoutPlanUiEvent.OnSaveWorkout(
                                workoutPlanSession = uiState.workoutPlanSession,
                            ),
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    bottom = 200.dp,
                ),
            ) {

                item(
                    key = "title_and_note",
                ) {
                    TitleAndNote(
                        modifier = Modifier
                            .padding(bottom = Dimens.Padding.Medium)
                            .fillMaxWidth(),
                    )
                }

                item(
                    key = "radar_chart"
                ) {
                    ExerciseTypeRadarChart()
                }

                uiState.workoutPlanSession?.exercisePlanSessions?.forEach { exercisePlanSession ->
                    exercisePlan(
                        exercisePlanSession = exercisePlanSession,
                        onAddSet = {
                            uiState.eventSink(
                                CreateWorkoutPlanUiEvent.OnAddSet(
                                    exercisePlanSession.exercisePlan.id,
                                ),
                            )
                        },
                        onSetWeightChanged = { id, weight ->
                            uiState.eventSink(
                                CreateWorkoutPlanUiEvent.OnSetWeightChanged(
                                    setId = id,
                                    setWeight = weight,
                                ),
                            )
                        },
                        onSetRepsChanged = { id, reps ->
                            uiState.eventSink(
                                CreateWorkoutPlanUiEvent.OnSetRepsChanged(
                                    setId = id,
                                    setReps = reps,
                                ),
                            )
                        },
                    )
                }

                if (uiState.workoutPlanSession == null) {
                    item(
                        key = "empty_workout",
                    ) {
                        EmptyWorkout(
                            modifier = Modifier,
                        )
                    }
                }


                item(
                    key = "add_exercise",
                ) {
                    AddExercise(
                        onAddExercise = {
                            uiState.eventSink(CreateWorkoutPlanUiEvent.OnAddExercise)
                        },
                        modifier = Modifier
                            .padding(top = Dimens.Padding.Medium)
                            .padding(horizontal = Dimens.Padding.Medium),
                    )
                }

            }
        }
    }
}

@Composable
private fun TitleAndNote(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = Dimens.Padding.Medium),
    ) {
        val titleTextState = rememberTextFieldState()
        BasicTextField(
            state = titleTextState,
            textStyle = MaterialTheme.typography.headlineSmall
                .copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            decorator = { innerTextField ->
                if (titleTextState.text.isEmpty()) {
                    Text(
                        text = "Workout Title",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }
                innerTextField()
            },
        )

        val noteTextState = rememberTextFieldState()
        BasicTextField(
            state = noteTextState,
            textStyle = MaterialTheme.typography.bodyMedium
                .copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            decorator = { innerTextField ->
                if (noteTextState.text.isEmpty()) {
                    Text(
                        text = "Add a note...",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        ),
                    )
                }
                innerTextField()
            },
            modifier = Modifier.padding(
                top = Dimens.Padding.Small,
            ),
        )
    }
}


private fun LazyListScope.exercisePlan(
    exercisePlanSession: ExercisePlanSession,
    onAddSet: () -> Unit,
    onSetWeightChanged: (id: Long, weight: Int) -> Unit,
    onSetRepsChanged: (id: Long, reps: Int) -> Unit,
) {
    item(
        key = exercisePlanSession.exercisePlan.id.toString() + exercisePlanSession.exercise.name,
    ) {
        ExercisePlan(
            name = exercisePlanSession.exercise.name,
            imageUrl = exercisePlanSession.exercise.imageUrl,
            modifier = Modifier
                .padding(top = Dimens.Padding.Medium),
        )
    }

    item(
        key = exercisePlanSession.exercisePlan.id.toString() + "titles",
    ) {
        SetColumnTitles(
            modifier = Modifier,
        )
    }

    itemsIndexed(
        key = { _, item -> "set_" + item.id },
        items = exercisePlanSession.setPlans,
    ) { index, setPlan ->
        SetPlanUi(
            index = index,
            id = setPlan.id,
            weight = setPlan.weight,
            reps = setPlan.reps,
            onSetRepsChanged = {
                onSetRepsChanged(setPlan.id, it)
            },
            onSetWeightChanged = {
                onSetWeightChanged(setPlan.id, it)
            },
            modifier = Modifier,
        )
    }

    item(
        key = exercisePlanSession.exercisePlan.id.toString() + "add_set",
    ) {
        AddNewSet(
            onAddSet = {
                onAddSet()
            },
        )
    }

}

@Composable
private fun CreateWorkoutPlanTopBar(
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = Dimens.Padding.Medium,
                )
                .padding(
                    end = Dimens.Padding.Medium,
                    start = Dimens.Padding.Small,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {

                },
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(align = Alignment.Start),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    modifier = Modifier,
                    contentDescription = "",
                )
            }

            Button(
                onClick = {
                    onSave()
                },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(align = Alignment.End),
                contentPadding = PaddingValues(
                    vertical = 8.dp,
                    horizontal = 16.dp,
                ),
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
fun AddExercise(
    onAddExercise: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        onClick = {
            onAddExercise()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
        ),
    ) {
        Text("+ Add Exercise")
    }
}

@Composable
fun ExercisePlan(
    name: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens.Padding.Medium),
        modifier = modifier
            .padding(horizontal = Dimens.Padding.Medium),
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(8.dp))
                .width(48.dp)
                .aspectRatio(2 / 3f)
                .background(Color.White),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
            )

            val noteTextState = rememberTextFieldState()

            BasicTextField(
                state = noteTextState,
                textStyle = MaterialTheme.typography.labelMedium
                    .copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                decorator = { innerTextField ->
                    if (noteTextState.text.isEmpty()) {
                        Text(
                            text = "Add a note...",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            ),
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier.padding(
                    top = Dimens.Padding.ExtraSmall,
                ),
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
            .padding(vertical = Dimens.Padding.Small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Set",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            modifier = Modifier
                .weight(1f),
        )
        Text(
            text = "Kg",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f),
        )
        Text(
            text = "Reps",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f),
        )
    }
}

@Composable
fun SetPlanUi(
    index: Int,
    id: Long,
    weight: Int,
    reps: Int,
    onSetWeightChanged: (weight: Int) -> Unit,
    onSetRepsChanged: (reps: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.Padding.Small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = (index + 1).toString(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f),
        )

        var setWeight: String by remember {
            mutableStateOf(weight.takeIf { it != -1 }?.toString() ?: "")
        }

        BasicTextField(
            value = setWeight,
            onValueChange = {
                if (pattern.matches(it)) {
                    setWeight = it
                    onSetWeightChanged(it.toIntOrNull() ?: SetPlan.Undefined)
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
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .wrapContentWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    innerTextField()

                    if (setWeight.isEmpty()) {
                        Text(
                            text = "0",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                            ),
                        )
                    }
                }

            },
            modifier = Modifier
                .wrapContentWidth(Alignment.CenterHorizontally)
                .weight(1f)
                .width(IntrinsicSize.Min)
                .defaultMinSize(24.dp),
        )

        var setReps by remember {
            mutableStateOf(reps.takeIf { it != -1 }?.toString() ?: "")
        }
        BasicTextField(
            value = setReps,
            onValueChange = {
                if (pattern.matches(it)) {
                    setReps = it
                    onSetRepsChanged(it.toIntOrNull() ?: SetPlan.Undefined)
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
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .wrapContentWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    innerTextField()

                    if (setReps.isEmpty()) {
                        Text(
                            text = "0",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                            ),
                        )
                    }
                }

            },
            modifier = Modifier
                .wrapContentWidth(Alignment.CenterHorizontally)
                .weight(1f)
                .width(IntrinsicSize.Min)
                .defaultMinSize(24.dp),
        )
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
    }
}
