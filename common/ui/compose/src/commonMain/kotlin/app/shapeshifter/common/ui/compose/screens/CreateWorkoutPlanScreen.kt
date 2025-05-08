package app.shapeshifter.common.ui.compose.screens

import com.slack.circuit.runtime.screen.Screen

@Parcelize
data class CreateWorkoutPlanScreen(
    val intent: Intent,
) : Screen {

    @Parcelize
    sealed interface Intent: Parcelable {
        @Parcelize
        data class NewWorkoutPlan(
            val routineId: Long,
        ) : Intent

        @Parcelize
        data class EditWorkoutPlan(
            val workoutPlanId: Long,
        ) : Intent
    }
}
