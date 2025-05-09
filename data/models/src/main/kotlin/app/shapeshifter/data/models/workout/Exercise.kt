package app.shapeshifter.data.models.workout

import kotlin.time.Duration

interface Exercise {
    val id: Long
    val exerciseTemplateId: Long
    val workoutId: Long
    val note: String?
    val restDuration: Duration?
    val index: Int
}

data class ExerciseLog(
    override val id: Long,
    override val workoutId: Long,
    override val exerciseTemplateId: Long,
    override val note: String?,
    override val restDuration: Duration?,
    override val index: Int,
    val exercisePlanId: Long,
) : Exercise

data class ExercisePlan(
    override val id: Long,
    override val workoutId: Long,
    override val exerciseTemplateId: Long,
    override val note: String?,
    override val restDuration: Duration?,
    override val index: Int,
) : Exercise
