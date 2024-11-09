package app.shapeshifter.feature.profile.ui

import androidx.compose.runtime.Immutable
import app.shapeshifter.data.models.workoutlog.WorkoutSession
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class ProfileUiState(
    val workouts: List<WorkoutSession> = emptyList(),
    val eventSink: (ProfileUiEvent)-> Unit,
) : CircuitUiState

sealed interface  ProfileUiEvent{
    data object GoBack: ProfileUiEvent
}


