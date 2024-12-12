package app.shapeshifter.feature.workout.ui.exercisesequence

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class ExerciseSequenceUiState(
    val eventSink: (ExerciseSequenceUiEvent) -> Unit,
) : CircuitUiState

sealed interface ExerciseSequenceUiEvent : CircuitUiEvent {
}
