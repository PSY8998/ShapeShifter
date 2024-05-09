package app.shapeshifter.feature.workout.ui.trackworkout

import app.shapeshifter.data.models.workoutlog.WorkoutSession
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class TrackWorkoutUiState(
    val workoutSession: WorkoutSession? = null,
    val eventSink: (TrackWorkoutUiEvent) -> Unit,
) : CircuitUiState

sealed interface TrackWorkoutUiEvent : CircuitUiEvent {
    data object GoBack : TrackWorkoutUiEvent

    data object OnAddExercise : TrackWorkoutUiEvent

    data object DiscardWorkout : TrackWorkoutUiEvent
}
