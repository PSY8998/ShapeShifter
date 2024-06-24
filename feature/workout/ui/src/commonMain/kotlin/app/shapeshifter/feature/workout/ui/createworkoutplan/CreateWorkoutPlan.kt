package app.shapeshifter.feature.workout.ui.createworkoutplan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.NestedScaffold
import app.shapeshifter.common.ui.compose.resources.Dimens
import app.shapeshifter.common.ui.compose.screens.CreateWorkoutPlanScreen
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

            LazyColumn {
                items(uiState.workoutPlanSession.exercisePlanSessions){
                    ExercisePlan(it.exercise.name)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding())
            ){
                AddExercise(
                    onAddExercise = {
                        uiState.eventSink(CreateWorkoutPlanUiEvent.OnAddExercise)
                    }
                )
            }
        }
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
    name : String
){
    Column {
        Text(name)
    }
}
