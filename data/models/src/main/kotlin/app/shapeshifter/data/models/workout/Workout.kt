package app.shapeshifter.data.models.workout

import kotlinx.datetime.Instant

interface Workout {
    val id: Long
    val name: String
    val note: String?
}

data class WorkoutLog(
    override val id: Long,
    override val name: String,
    override val note: String?,
    val workoutPlanId: Long?,
    val startTime: Instant,
    val finishTime: Instant?,
) : Workout {
    companion object {
        fun empty(
            name: String,
            startTime: Instant,
        ) = WorkoutLog(
            id = 0,
            name = "",
            note = null,
            workoutPlanId = null,
            startTime = startTime,
            finishTime = null,
        )
    }
}

data class WorkoutPlan(
    override val id: Long,
    override val name: String,
    override val note: String?,
    val routineId: Long,
) : Workout
