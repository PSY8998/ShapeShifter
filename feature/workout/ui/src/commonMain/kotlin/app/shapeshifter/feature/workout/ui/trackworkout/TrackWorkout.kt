package app.shapeshifter.feature.workout.ui.trackworkout

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import app.shapeshifter.common.ui.compose.screens.TrackWorkoutScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject

@Inject
class TrackWorkoutUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is TrackWorkoutScreen -> ui<TrackWorkoutUiState> { state, modifier ->
                TrackWorkout()
            }
            else -> null
        }
    }
}

@Composable
private fun TrackWorkout(){
    Text("Workout in progress")
}
