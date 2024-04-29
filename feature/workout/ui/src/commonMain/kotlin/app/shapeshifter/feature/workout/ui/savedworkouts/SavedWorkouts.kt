package app.shapeshifter.feature.workout.ui.savedworkouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.shapeshifter.common.ui.compose.screens.SavedWorkoutsScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject

@Inject
class SavedWorkoutsUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is SavedWorkoutsScreen -> ui<SavedWorkoutsUiState> { state, modifier ->
                SavedWorkouts(state)
            }
            else -> null
        }
    }
}

@Composable
private fun SavedWorkouts(
    uiState: SavedWorkoutsUiState
){
    val eventSink = uiState.eventSink

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ){ paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            QuickWorkoutButton (
                modifier = Modifier,
                onStart = {
                    eventSink(SavedWorkoutsUiEvent.OpenQuickWorkout)
                }
            )
        }
    }
}

@Composable
private fun QuickWorkoutButton(
    modifier: Modifier = Modifier,
    onStart: () -> Unit,
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Button(
            onClick = {
                onStart()
            }
        ){
            Text("Quick Workout")
        }
    }
}
