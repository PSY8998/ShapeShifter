package app.shapeshifter.data.models.workoutlog

import app.shapeshifter.data.models.workout.WorkoutLog

data class WorkoutSessionOverview(
    val routine: RoutineOverview,
    val plan: WorkoutPlanOverview?,
    val workout: WorkoutLog,
) {
    data class RoutineOverview(
        val id: Long,
        val name: String,
    )

    data class WorkoutPlanOverview(
        val id: Long,
        val name: String,
    )
}
