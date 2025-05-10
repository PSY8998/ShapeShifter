package app.shapeshifter.feature.profile.ui

import androidx.compose.runtime.Immutable
import app.shapeshifter.data.models.workout.WorkoutLogSession
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class ProfileUiState(
    val workouts: List<WorkoutLogSession> = emptyList(),
    val eventSink: (ProfileUiEvent)-> Unit,
) : CircuitUiState

sealed interface  ProfileUiEvent{
    data object GoBack: ProfileUiEvent
}


