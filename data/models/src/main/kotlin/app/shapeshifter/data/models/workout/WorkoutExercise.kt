package app.shapeshifter.data.models.workout

import app.shapeshifter.data.models.Entity

data class WorkoutExercise(
    override val id: Long,
    val workoutId: Long,
    val exerciseId: Long,
    val note: String,
): Entity
