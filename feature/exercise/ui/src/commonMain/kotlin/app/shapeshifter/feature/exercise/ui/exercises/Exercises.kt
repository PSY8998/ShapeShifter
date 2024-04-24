package app.shapeshifter.feature.exercise.ui.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.NestedScaffold
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import app.shapeshifter.feature.exercise.data.models.Exercise
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import shapeshifter.feature.exercise.ui.generated.resources.Res
import shapeshifter.feature.exercise.ui.generated.resources.exercise_deadlift

@Inject
class ExercisesUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is ExercisesScreen -> ui<ExercisesState> { state, _ ->
                Exercises(state)
            }

            else -> null
        }
    }
}

@Composable
fun Exercises(
    state: ExercisesState,
) {
    NestedScaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Exercises",
                modifier = Modifier
                    .padding(8.dp),
                style = MaterialTheme.typography.titleLarge,
            )

            when (state) {
                is ExercisesState.Exercises -> {
                    ExercisesContent(
                        state = state,
                        openCreateExercise = {
                            state.eventSink(ExerciseUiEvent.OpenCreateExercise)
                        },
                        modifier = Modifier
                            .weight(1f),
                    )
                }

                is ExercisesState.Empty -> {}
            }
        }
    }
}

@Composable
private fun ExercisesContent(
    state: ExercisesState.Exercises,
    openCreateExercise: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        ExerciseScrollContent(
            modifier = Modifier
                .fillMaxSize(),
            exercises = state.exercises,
        )

        Button(
            onClick = {
                openCreateExercise()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter),
        ) {
            Text("Create Exercise")
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ExerciseScrollContent(
    modifier: Modifier = Modifier,
    exercises: List<Exercise>,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(exercises) {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(8.dp),
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(Res.drawable.exercise_deadlift),
                        contentDescription = "exercise image",
                        modifier = Modifier,
                    )

                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        text = it.name,
                        textAlign = TextAlign.End,
                    )
                }
            }
        }
    }
}

