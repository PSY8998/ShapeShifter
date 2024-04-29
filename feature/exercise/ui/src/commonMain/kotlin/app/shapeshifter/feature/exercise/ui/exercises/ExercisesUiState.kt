package app.shapeshifter.feature.exercise.ui.exercises

import app.shapeshifter.data.models.Exercise
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

sealed interface ExercisesUiState : CircuitUiState {

    val eventSink: (ExerciseUiEvent) -> Unit

    data class Exercises(
        override val eventSink: (ExerciseUiEvent) -> Unit,
        val exercises: List<app.shapeshifter.data.models.Exercise>,
    ) : ExercisesUiState

    data class Empty(
        override val eventSink: (ExerciseUiEvent) -> Unit,
    ) : ExercisesUiState
}


sealed interface ExerciseUiEvent : CircuitUiEvent {
    data object OpenCreateExercise : ExerciseUiEvent
}
