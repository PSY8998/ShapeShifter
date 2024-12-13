package app.shapeshifter.feature.workout.ui.exercisesequence

import androidx.compose.runtime.Immutable
import app.shapeshifter.data.models.workoutlog.ExerciseSession
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
sealed interface ExerciseSequenceUiState : CircuitUiState {
    val eventSink: (ExerciseSequenceUiEvent) -> Unit

    @Immutable
    data class Empty(
        override val eventSink: (ExerciseSequenceUiEvent) -> Unit
    ) : ExerciseSequenceUiState

    @Immutable
    data class Filled(
        val exerciseSessions: List<ExerciseSession>,
        override val eventSink: (ExerciseSequenceUiEvent) -> Unit
    ) : ExerciseSequenceUiState
}

sealed interface ExerciseSequenceUiEvent : CircuitUiEvent {
    data class OnReorderedExercises(
        val exerciseSessions: List<ExerciseSession>
    ) : ExerciseSequenceUiEvent
}
