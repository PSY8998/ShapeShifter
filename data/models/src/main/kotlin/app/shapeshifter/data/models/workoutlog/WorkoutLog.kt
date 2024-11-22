package app.shapeshifter.data.models.workoutlog

import app.shapeshifter.data.models.Entity
import app.shapeshifter.data.models.plans.WorkoutPlan
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private const val UnfinishedWorkoutTimeInMillis = 0L

data class WorkoutLog(
    override val id: Long,
    val workoutPlanId: Long,
    val name: String,
    val startTimeInMillis: Long,
    val finishTimeInMillis: Long,
    val note: String,
    val restFinishTimeInMillis: Long,
) : Entity {

    fun isWorkoutFinished() = finishTimeInMillis != UnfinishedWorkoutTimeInMillis

    fun formatMillisToDate(): String{
        val formatter = DateTimeFormatter.ofPattern("EEEE, d MMM, yyyy")
        val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTimeInMillis), ZoneId.systemDefault())
        return dateTime.format(formatter)
    }

    companion object {
        fun emptyQuickWorkout(): WorkoutLog {
            return WorkoutLog(
                id = 0,
                workoutPlanId = WorkoutPlan.QuickWorkoutId,
                name = "Quick Workout",
                startTimeInMillis = System.currentTimeMillis(),
                finishTimeInMillis = UnfinishedWorkoutTimeInMillis,
                note = "",
                restFinishTimeInMillis = 0,
            )
        }
    }
}
