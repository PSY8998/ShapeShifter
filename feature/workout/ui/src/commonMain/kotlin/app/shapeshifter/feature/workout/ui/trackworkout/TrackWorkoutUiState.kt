package app.shapeshifter.feature.workout.ui.trackworkout

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import app.shapeshifter.data.models.workoutlog.SetLog
import app.shapeshifter.data.models.workoutlog.WorkoutLog
import app.shapeshifter.data.models.workoutlog.WorkoutSession
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import java.sql.Time

@Immutable
sealed interface TrackWorkoutUiState : CircuitUiState {
    val eventSink: (TrackWorkoutUiEvent) -> Unit

    @Immutable
    data class Initial(
        override val eventSink: (TrackWorkoutUiEvent) -> Unit,
    ) : TrackWorkoutUiState

    @Immutable
    data class Empty(
        override val eventSink: (TrackWorkoutUiEvent) -> Unit,
    ) : TrackWorkoutUiState

    @Immutable
    data class Filled(
        val workoutSession: WorkoutSession,
        override val eventSink: (TrackWorkoutUiEvent) -> Unit,
    ) : TrackWorkoutUiState

    fun asFilled(): Filled? = this as? Filled
}

sealed interface TrackWorkoutUiEvent : CircuitUiEvent {
    data object GoBack : TrackWorkoutUiEvent

    data object OnAddExercise : TrackWorkoutUiEvent

    data object DiscardWorkout : TrackWorkoutUiEvent

    data class OnAddSet(
        val exerciseLogId: Long,
        val exerciseId: Long,
        val workoutPlanId: Long,
        val workoutLogId: Long,
    ) : TrackWorkoutUiEvent

    data class OnSetCompleted(
        val set: SetLog,
    ) : TrackWorkoutUiEvent

    data class OnDeleteSet(
        val set: SetLog,
    ) : TrackWorkoutUiEvent

    data class OnFinishWorkout(
        val workoutSession: WorkoutSession,
    ) : TrackWorkoutUiEvent
}
