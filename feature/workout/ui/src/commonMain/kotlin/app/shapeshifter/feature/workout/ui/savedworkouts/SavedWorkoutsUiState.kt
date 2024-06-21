package app.shapeshifter.feature.workout.ui.savedworkouts

import app.shapeshifter.data.models.workoutlog.WorkoutLog
import app.shapeshifter.data.models.workoutlog.WorkoutSessionOverview
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class SavedWorkoutsUiState(
    val activeWorkout: WorkoutSessionOverview? = null,
    val eventSink: (SavedWorkoutsUiEvent) -> Unit,
) : CircuitUiState

sealed interface SavedWorkoutsUiEvent : CircuitUiEvent {
    data object OpenQuickWorkout : SavedWorkoutsUiEvent

    data class DiscardWorkout(val workoutLog: WorkoutLog) : SavedWorkoutsUiEvent

    data class DiscardAndStartNewWorkout(val workoutLog: WorkoutLog) : SavedWorkoutsUiEvent

    data class CreateWorkoutPlan(
        val routineId: Long,
        val planName: String,
    ) : SavedWorkoutsUiEvent
}
