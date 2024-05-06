package app.shapeshifter.data.models.workoutlog

import app.shapeshifter.data.models.Entity
import app.shapeshifter.data.models.PositiveInt

data class SetLog(
    override val id: Long,
    val workoutExerciseId: Long,
    val index: PositiveInt,
    val weight: PositiveInt,
    val reps: PositiveInt,
    val completed: Boolean,
) : Entity
