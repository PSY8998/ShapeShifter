package app.shapeshifter.feature.workout.ui.trackworkout

import androidx.compose.runtime.Immutable
import app.shapeshifter.data.models.workoutlog.ExerciseLog
import app.shapeshifter.data.models.workoutlog.SetLog
import app.shapeshifter.data.models.workoutlog.WorkoutSession
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

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
        val restTimeDurationInSecs: Long,
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
        val exerciseLog: ExerciseLog,
    ) : TrackWorkoutUiEvent

    data class OnDeleteSet(
        val set: SetLog,
    ) : TrackWorkoutUiEvent

    data class OnFinishWorkoutWithDate(
        val workoutSession: WorkoutSession,
        val selectedDate: Long,
    ) : TrackWorkoutUiEvent

    data class OnReorderExercises(
        val workoutLogId: Long,
    ) : TrackWorkoutUiEvent

    data class OnReplaceExercise(
        val exerciseLog: ExerciseLog,
    ) : TrackWorkoutUiEvent

    data class OnRemoveExercise(
        val exerciseLog: ExerciseLog,
    ): TrackWorkoutUiEvent

    data class OnUpdateRestTime(
        val minutes: Int,
        val seconds: Int,
        val exerciseLog: ExerciseLog,
    ): TrackWorkoutUiEvent

    data class OnUpdateSet(
        val setLog: SetLog,
    ): TrackWorkoutUiEvent

    data object OnCompleteRestTime: TrackWorkoutUiEvent
}
