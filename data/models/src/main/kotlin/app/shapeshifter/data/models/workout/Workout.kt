package app.shapeshifter.data.models.workout

interface Workout {
    val id: Long
    val name: String
    val note: String?
}

data class WorkoutLog(
    override val id: Long,
    override val name: String,
    override val note: String?,
    val workoutPlanId: Long,
) : Workout

data class WorkoutPlan(
    override val id: Long,
    override val name: String,
    override val note: String?,
    val routineId: Long,
) : Workout
