package app.shapeshifter.common.ui.compose.screens

import android.os.Parcelable
import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExercisesScreen(
    val intent: Intent,
) : Screen {

    sealed interface Intent : Parcelable {
        @Parcelize
        data object SelectExercises : Intent

        @Parcelize
        data class ReplaceExercise(val exerciseLogId: Long) : Intent

        @Parcelize
        data object Exercises : Intent
    }

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
