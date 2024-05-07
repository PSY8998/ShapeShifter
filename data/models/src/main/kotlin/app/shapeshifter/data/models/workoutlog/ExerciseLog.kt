package app.shapeshifter.data.models.workoutlog

import app.shapeshifter.data.models.Entity

data class ExerciseLog(
    override val id: Long,
    val workoutId: Long,
    val exerciseId: Long,
    val note: String,
) : Entity
