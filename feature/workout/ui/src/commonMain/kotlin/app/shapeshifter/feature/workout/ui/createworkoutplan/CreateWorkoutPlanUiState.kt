package app.shapeshifter.feature.workout.ui.createworkoutplan

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class CreateWorkoutPlanUiState(
    val planName: String,
) : CircuitUiState

sealed interface CreateWorkoutPlanUiEvent : CircuitUiEvent
