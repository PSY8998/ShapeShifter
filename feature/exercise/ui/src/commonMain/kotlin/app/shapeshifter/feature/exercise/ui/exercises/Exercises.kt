package app.shapeshifter.feature.exercise.ui.exercises

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.shapeshifter.common.ui.compose.NestedScaffold
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import app.shapeshifter.feature.exercise.ui.ExerciseDetail
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject

@Inject
class ExercisesUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is ExercisesScreen -> ui<ExercisesState> { _, _ ->
                Exercises()
            }

            else -> null
        }
    }
}

@Composable
fun Exercises() {
    NestedScaffold (
        modifier = Modifier.fillMaxSize()
    ){
        Column {
            Text("Exercises")
        }
    }
}
