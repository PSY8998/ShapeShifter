package app.shapeshifter.data.models.workoutlog

import app.shapeshifter.data.models.Entity

data class ExerciseLog(
    override val id: Long,
    val workoutLogId: Long,
    val workoutPlanId: Long,
    val exerciseId: Long,
    val exercisePlanId: Long?,
    val note: String,
    val restTimeDuration: Long,
) : Entity
