package app.shapeshifter.common.ui.compose.screens

import android.os.Parcelable
import app.shapeshifter.data.models.plans.WorkoutPlanSession
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class CreateWorkoutPlanScreen(
    val intent: Intent,
) : Screen {

    @Parcelize
    sealed interface Intent : Parcelable {
        @Parcelize
        data class NewWorkoutPlan(
            val planName: String,
            val routineId: Long,
        ) : Intent

        @Parcelize
        data class EditWorkoutPlan(
            val workoutPlanId: Long,
        ) : Intent
    }
}
