package app.shapeshifter.data.models.routines

import app.shapeshifter.data.models.Entity
import app.shapeshifter.data.models.PositiveInt

data class SavedWorkoutExerciseSet(
    override val id: Long,
    val workoutExerciseId: Long,
    val index: PositiveInt,
    val weight: PositiveInt,
    val reps: PositiveInt,
) : Entity
