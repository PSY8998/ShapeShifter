package app.shapeshifter.data.models.plans

import app.shapeshifter.data.models.Entity

data class WorkoutPlan(
    override val id: Long,
    val routineId: Long,
    val name: String,
) : Entity {

    companion object {
        const val QuickWorkoutId: Long = -1L
    }
}
