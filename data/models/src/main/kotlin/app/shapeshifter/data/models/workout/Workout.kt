package app.shapeshifter.data.models.workout

import app.shapeshifter.data.models.Entity
import app.shapeshifter.data.models.routines.SavedWorkout

private const val UnfinishedWorkoutTimeInMillis = 0L

data class Workout(
    override val id: Long,
    val savedWorkoutId: Long,
    val startTimeInMillis: Long,
    val finishTimeInMillis: Long,
    val note: String,
) : Entity {

    fun isWorkoutFinished() = finishTimeInMillis != UnfinishedWorkoutTimeInMillis

    companion object {
        fun emptyQuickWorkout(): Workout {
            return Workout(
                id = 0,
                savedWorkoutId = SavedWorkout.QuickWorkoutId,
                startTimeInMillis = System.currentTimeMillis(),
                finishTimeInMillis = UnfinishedWorkoutTimeInMillis,
                note = "",
            )
        }
    }
}
