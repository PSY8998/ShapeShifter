package app.shapeshifter.data.models.plans

import app.shapeshifter.data.models.Entity
import app.shapeshifter.data.models.PositiveInt

data class SetPlan(
    override val id: Long,
    val exercisePlanId: Long,
    val index: PositiveInt,
    val weight: Int,
    val reps: Int,
) : Entity {

    companion object {
        const val Undefined = -1
    }
}
