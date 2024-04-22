package app.shapeshifter.feature.exercise.ui.exercises

import app.shapeshifter.feature.exercise.data.models.Exercise
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

sealed interface ExercisesState : CircuitUiState {
    data class Exercises(
        val eventSink: (ExerciseUiEvent) -> Unit,
        val exercises: List<Exercise>,
    ) : ExercisesState

    data class Empty(
        val eventSink: (ExerciseUiEvent.OpenCreateExercise) -> Unit,
    ) : ExercisesState
}


sealed interface ExerciseUiEvent : CircuitUiEvent {
    data object OpenCreateExercise : ExerciseUiEvent
}
