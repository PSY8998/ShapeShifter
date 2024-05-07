package app.shapeshifter.data.models.plans

import app.shapeshifter.data.models.Entity
import app.shapeshifter.data.models.PositiveInt

data class ExercisePlan(
    override val id: Long,
    val workoutPlanId: Long,
    val exerciseId: Long,
    val index: PositiveInt,
) : Entity
