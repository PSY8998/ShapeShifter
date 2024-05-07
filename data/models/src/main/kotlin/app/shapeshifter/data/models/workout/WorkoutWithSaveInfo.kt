package app.shapeshifter.data.models.workout

data class WorkoutWithSaveInfo(
    val workout: Workout,
    val name: String,
    val savedWorkoutId: Long,
)
