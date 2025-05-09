package app.shapeshifter.data.models.workout

import kotlin.time.Duration

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
    val startTime: Duration,
    val finishTime: Duration?,
) : Workout {
    companion object {
        fun empty(name: String, startTime: Duration) = WorkoutLog(
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
