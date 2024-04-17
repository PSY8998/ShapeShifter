package app.shapeshifter.feature.exercise.ui.exercises

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class ExercisesState(
    val eventSink: (ExerciseUiEvent) -> Unit,
) : CircuitUiState

sealed interface ExerciseUiEvent : CircuitUiEvent {
    data object OpenCreateExercise : ExerciseUiEvent
}
