package app.shapeshifter.feature.workout.ui.exercisesequence

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.shapeshifter.common.ui.compose.screens.ExerciseSequenceScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject

@Inject
class ExerciseSequenceUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is ExerciseSequenceScreen -> ui<ExerciseSequenceUiState> { state, modifier ->
                ReorderExercises(
                    uiState = state,
                    modifier = modifier,
                )
            }

            else -> null
        }
    }
}

@Composable
internal fun ReorderExercises(
    uiState: ExerciseSequenceUiState,
    modifier: Modifier = Modifier,
) {
    Text("Reorder here")
}
