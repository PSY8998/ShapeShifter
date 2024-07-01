package app.shapeshifter.feature.workout.ui.createworkoutplan

import app.shapeshifter.data.models.plans.ExercisePlanSession
import app.shapeshifter.data.models.plans.WorkoutPlanSession
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class CreateWorkoutPlanUiState(
    val workoutPlanSession: WorkoutPlanSession,
    val eventSink: (CreateWorkoutPlanUiEvent) -> Unit,
) : CircuitUiState

sealed interface CreateWorkoutPlanUiEvent : CircuitUiEvent {

    data object OnAddExercise : CreateWorkoutPlanUiEvent

    data class OnAddSet(
        val exerciseId: Long,
    ) : CreateWorkoutPlanUiEvent
}
