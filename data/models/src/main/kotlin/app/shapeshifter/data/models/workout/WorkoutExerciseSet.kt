package app.shapeshifter.data.models.workout

import app.shapeshifter.data.models.Entity
import app.shapeshifter.data.models.PositiveInt

data class WorkoutExerciseSet(
    override val id: Long,
    val index: PositiveInt,
    val weight: PositiveInt,
    val reps: PositiveInt,
    val completed: Boolean,
) : Entity
