package app.shapeshifter.feature.workout.ui.postworkout

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

// Define events for user interactions
sealed interface PostWorkoutEvent : CircuitUiEvent {
    data object ShareProgress : PostWorkoutEvent
    data object ViewHistory : PostWorkoutEvent
    data object ReturnHome : PostWorkoutEvent
    data object LogNotes : PostWorkoutEvent
}

@Immutable
data class PostWorkoutUiState(
    val totalTime: String = "00:00", // Placeholder, replace with actual data source
    val caloriesBurned: Int? = null, // Placeholder
    val exerciseCount: Int = 0, // Placeholder
    val setCount: Int = 0, // Placeholder
    val hasNewPR: Boolean = false, // Placeholder
    val eventSink: (PostWorkoutEvent) -> Unit,
) : CircuitUiState
