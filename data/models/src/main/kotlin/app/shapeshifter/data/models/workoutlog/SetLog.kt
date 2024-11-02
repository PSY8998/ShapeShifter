package app.shapeshifter.data.models.workoutlog

import app.shapeshifter.data.models.Entity
import app.shapeshifter.data.models.PositiveInt

data class SetLog(
    override val id: Long,
    val exerciseLogId: Long,
    val index: PositiveInt,
    val weight: PositiveInt,
    val reps: PositiveInt,
    val prevReps: PositiveInt,
    val prevWeight: PositiveInt,
    val completed: Boolean,
    val finishTime: Long,
) : Entity {

    fun isValid(): Boolean {
        if (weight.value < 0) {
            return false
        }

        if (reps.value <= 0) {
            return false
        }

        if (finishTime <= 0) {
            return false
        }

        return true
    }
}
