package app.shapeshifter.feature.workout.ui.savedworkouts

import androidx.compose.runtime.Immutable
import app.shapeshifter.data.models.plans.WorkoutPlanSession
import app.shapeshifter.data.models.workout.WorkoutLog
import app.shapeshifter.data.models.workoutlog.WorkoutSessionOverview
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class SavedWorkoutsUiState(
    val activeWorkout: WorkoutSessionOverview? = null,
    val workoutPlans: List<WorkoutPlanSession> = emptyList(),
    val eventSink: (SavedWorkoutsUiEvent) -> Unit,
) : CircuitUiState

sealed interface SavedWorkoutsUiEvent : CircuitUiEvent {

    data object SwitchToHome : SavedWorkoutsUiEvent

    data object OpenQuickWorkout : SavedWorkoutsUiEvent

    data class DiscardWorkout(val workoutLog: WorkoutLog) : SavedWorkoutsUiEvent

    data class DiscardAndStartNewWorkout(val workoutLog: WorkoutLog) : SavedWorkoutsUiEvent

    data class CreateWorkoutPlan(
        val routineId: Long,
    ) : SavedWorkoutsUiEvent

    data class OnEditWorkoutPlan(
        val workoutPlanSession: WorkoutPlanSession,
    ) : SavedWorkoutsUiEvent

    data class OnDeleteWorkoutPlan(
        val workoutPlanSession: WorkoutPlanSession,
    ) : SavedWorkoutsUiEvent

    data class OnStartWorkoutPlan(
        val workoutPlanSession: WorkoutPlanSession,
    ) : SavedWorkoutsUiEvent
}
