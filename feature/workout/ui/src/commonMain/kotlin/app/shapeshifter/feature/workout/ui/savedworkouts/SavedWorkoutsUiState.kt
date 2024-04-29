package app.shapeshifter.feature.workout.ui.savedworkouts

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class SavedWorkoutsUiState(
    val eventSink: (SavedWorkoutsUiEvent) -> Unit,
) : CircuitUiState

sealed interface SavedWorkoutsUiEvent : CircuitUiEvent {
    data object OpenQuickWorkout : SavedWorkoutsUiEvent
}
