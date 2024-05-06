package app.shapeshifter.data.models.routines

import app.shapeshifter.data.models.Entity
import app.shapeshifter.data.models.PositiveInt

data class SavedWorkoutExercise(
    override val id: Long,
    val workoutId: Long,
    val exerciseId: Long,
    val index: PositiveInt,
) : Entity
