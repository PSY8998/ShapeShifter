package app.shapeshifter.feature.workout.ui.createworkoutplan

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
                planName = uiState.workoutPlanSession.workoutPlan.name,
                modifier = Modifier
                    .fillMaxWidth(),
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                uiState.workoutPlanSession.exercisePlanSessions.forEach{exercisePlanSession ->
                    exercisePlan(
                        exercisePlanSession = exercisePlanSession,
                        onAddSet = {
                            uiState.eventSink(CreateWorkoutPlanUiEvent.OnAddSet(exercisePlanSession.exercise.id))
                        }
                    )
                }

                item {
                    AddExercise(
                        onAddExercise = {
                            uiState.eventSink(CreateWorkoutPlanUiEvent.OnAddExercise)
                        }
                    )
                }

            }
        }
    }
}


private fun LazyListScope.exercisePlan(
    exercisePlanSession: ExercisePlanSession,
    onAddSet: () -> Unit,
){
    item {
        ExercisePlan(exercisePlanSession.exercise.name)
    }

    itemsIndexed(
        items = exercisePlanSession.setPlans
    ) { _, setPlan ->
        SetPlanUi(
            index = setPlan.index.value,
            weight = setPlan.weight.value,
            reps = setPlan.reps.value,
            modifier = Modifier,
        )
    }

    item {
        AddNewSet(
            onAddSet = {
                onAddSet()
            }
        )
    }

}

@Composable
private fun CreateWorkoutPlanTopBar(
    planName: String,
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

            Column(
                modifier = Modifier
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = planName,
                    modifier = Modifier,
                )
            }

            Button(
                onClick = {
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
) {
    Column {
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
fun SetPlanUi(
    index: Int,
    weight: Int,
    reps: Int,
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
            text = (index + 1).toString(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f),
        )

        var setWeight: String by remember {
            mutableStateOf(weight.takeIf { it != 0 }?.toString() ?: "")
        }

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
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .wrapContentWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    innerTextField()
                }
            },
            modifier = Modifier
                .wrapContentWidth(Alignment.CenterHorizontally)
                .weight(1f)
                .width(IntrinsicSize.Min)
                .defaultMinSize(24.dp),
        )

        var setReps by remember {
            mutableStateOf(reps.takeIf { it != 0 }?.toString() ?: "")
        }

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
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .wrapContentWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    innerTextField()
                }
            },
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
