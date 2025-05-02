package app.shapeshifter.data.models.workoutlog

import app.shapeshifter.data.models.Entity
import app.shapeshifter.data.models.PositiveInt

data class SetLog(
    override val id: Long,
    val setIndex: PositiveInt,
    val exerciseLogId: Long,
    val exercisePlanId: Long?,
    val exerciseId: Long,
    val workoutPlanId: Long,
    val workoutLogId: Long,
    val weight: PositiveInt,
    val reps: PositiveInt,
    val prevReps: PositiveInt,
    val prevWeight: PositiveInt,
    val completed: Boolean,
    val finishTime: Long,
    val setTypeId: Long = 2,
) : Entity {

    fun isValid(): Boolean {
        if (weight.value < 0) {
            return false
        }

        if (reps.value <= 0) {
            return false
        }

        return true
    }
}
