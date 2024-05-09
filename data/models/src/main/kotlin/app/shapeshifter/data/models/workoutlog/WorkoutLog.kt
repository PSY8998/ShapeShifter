package app.shapeshifter.data.models.workoutlog

import app.shapeshifter.data.models.Entity
import app.shapeshifter.data.models.plans.WorkoutPlan

private const val UnfinishedWorkoutTimeInMillis = 0L

data class WorkoutLog(
    override val id: Long,
    val workoutPlanId: Long,
    val startTimeInMillis: Long,
    val finishTimeInMillis: Long,
    val note: String,
) : Entity {

    fun isWorkoutFinished() = finishTimeInMillis != UnfinishedWorkoutTimeInMillis

    companion object {
        fun emptyQuickWorkout(): WorkoutLog {
            return WorkoutLog(
                id = 0,
                workoutPlanId = WorkoutPlan.QuickWorkoutId,
                startTimeInMillis = System.currentTimeMillis(),
                finishTimeInMillis = UnfinishedWorkoutTimeInMillis,
                note = "",
            )
        }
    }
}
