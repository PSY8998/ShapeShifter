package app.shapeshifter.feature.exercise.ui

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import me.tatarka.inject.annotations.Inject

data class ExerciseDetailState(
    val eventSink: (ExerciseDetailUiEvent) -> Unit
) : CircuitUiState

@Inject
sealed interface ExerciseDetailUiEvent : CircuitUiEvent {
    data object GoBack : ExerciseDetailUiEvent
    data class CreateExercise(
        val exerciseName: String
    ) : ExerciseDetailUiEvent
}
