package app.shapeshifter.data.models.workout

interface Set {
    val id: Long
    val weight: Weight
    val reps: Int
    val exerciseId: Long
}

data class SetLog(
    override val id: Long,
    override val weight: Weight,
    override val reps: Int,
    override val exerciseId: Long,
    val setPlanId: Long,
    val isCompleted: Boolean,
    val previousWeight: Weight,
    val previousReps: Int,
) : Set

data class SetPlan(
    override val id: Long,
    override val weight: Weight,
    override val reps: Int,
    override val exerciseId: Long,
) : Set

@JvmInline
value class Weight(val value: Float) {
    init {
        require(value >= 0) { "Weight must be non-negative" }
    }
}

@JvmInline
value class Reps(val value: Int) {
    init {
        require(value >= 0) { "Reps must be non-negative" }
    }
}
