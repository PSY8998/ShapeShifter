package app.shapeshifter.feature.workout.ui.postworkout

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

// Define events for user interactions
sealed interface PostWorkoutEvent : CircuitUiEvent {
    data object ShareProgress : PostWorkoutEvent
    // data object ViewHistory : PostWorkoutEvent // Removed as per request
    // data object ReturnHome : PostWorkoutEvent // Removed as per request
    data object LogNotes : PostWorkoutEvent
}

@Immutable
data class ExerciseInfo(
    val id: Long, // Keep ID for potential future use (e.g., navigation)
    val name: String,
    // TODO: Add icon identifier (e.g., iconRes: Int or iconUrl: String) if available
)

@Immutable
data class RecordInfo(
    val name: String, // e.g., "Heaviest Deadlift"
    val value: String, // e.g., "200 lb"
    // TODO: Add icon identifier if available
)

@Immutable
data class PostWorkoutUiState(
    val totalTime: String = "00:00",
    val caloriesBurned: Int? = null,
    val setCount: Int = 0,
    val completedExercises: List<ExerciseInfo> = emptyList(),
    val achievedRecords: List<RecordInfo> = emptyList(),
    val eventSink: (PostWorkoutEvent) -> Unit,
) : CircuitUiState {
    // Derived properties can be added here if needed, e.g.,
    // val exerciseCount: Int get() = completedExercises.size
    // val hasNewPR: Boolean get() = achievedRecords.isNotEmpty()
}
