package app.shapeshifter.common.ui.compose.screens

import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen

@Parcelize
data class ExercisesScreen(
    val intent: Intent,
) : Screen, Parcelable {

    @Parcelize
    sealed interface Intent: Parcelable {
        data object SelectExercises : Intent

        data class ReplaceExercise(val exerciseLogId: Long) : Intent

        data object Exercises : Intent
    }

    @Parcelize
    sealed interface Result : PopResult {
        @Parcelize
        data class SelectedExercises(
            val exerciseIds: List<Long>,
        ) : Result

        @Parcelize
        data class ReplaceExercise(
            val exerciseLogId: Long,
            val exerciseId: Long,
        ) : Result
    }
}
