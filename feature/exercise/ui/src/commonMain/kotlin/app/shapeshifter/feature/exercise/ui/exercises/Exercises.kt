package app.shapeshifter.feature.exercise.ui.exercises

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.NestedScaffold
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject

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

            Box(
                modifier = Modifier
                    .weight(1f),
            ) {
                Button(
                    onClick = {
                        state.eventSink(ExerciseUiEvent.OpenCreateExercise)
                    },
                    modifier = Modifier
                        .align(Alignment.Center),
                    ) {
                    Text("Create Exercise")
                }
            }
        }
    }
}
