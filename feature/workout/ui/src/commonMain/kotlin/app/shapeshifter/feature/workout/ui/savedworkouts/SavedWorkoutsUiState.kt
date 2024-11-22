package app.shapeshifter.feature.workout.ui.savedworkouts

import androidx.compose.runtime.Immutable
import app.shapeshifter.data.models.workoutlog.WorkoutLog
import app.shapeshifter.data.models.workoutlog.WorkoutSessionOverview
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class SavedWorkoutsUiState(
    val activeWorkout: WorkoutSessionOverview? = null,
    val eventSink: (SavedWorkoutsUiEvent) -> Unit,
) : CircuitUiState

sealed interface SavedWorkoutsUiEvent : CircuitUiEvent {

    data object SwitchToHome : SavedWorkoutsUiEvent

    data object OpenQuickWorkout : SavedWorkoutsUiEvent

    data class DiscardWorkout(val workoutLog: WorkoutLog) : SavedWorkoutsUiEvent

    data class DiscardAndStartNewWorkout(val workoutLog: WorkoutLog) : SavedWorkoutsUiEvent

    data class CreateWorkoutPlan(
        val routineId: Long,
        val planName: String,
    ) : SavedWorkoutsUiEvent
}
