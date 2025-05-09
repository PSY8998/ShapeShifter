package app.shapeshifter.data.models.workout

interface Set {
    val id: Long
    val weight: Weight
    val reps: Reps
    val exerciseId: Long
    val index: Int
    val setTypeId: Int

    fun isValid(): Boolean {
        return reps.value > 0
    }
}

data class SetLog(
    override val id: Long,
    override val weight: Weight,
    override val reps: Reps,
    override val exerciseId: Long,
    override val index: Int,
    override val setTypeId: Int,
    val setPlanId: Long?,
    val isCompleted: Boolean,
    val previousWeight: Weight,
    val previousReps: Reps,
) : Set

data class SetPlan(
    override val id: Long,
    override val weight: Weight,
    override val reps: Reps,
    override val exerciseId: Long,
    override val index: Int,
    override val setTypeId: Int,
) : Set

@JvmInline
value class Weight(val value: Float) {
    init {
        require(value >= 0) { "Weight must be non-negative" }
    }

    companion object {
        val ZERO = Weight(0f)
    }
}

@JvmInline
value class Reps(val value: Int) {
    init {
        require(value >= 0) { "Reps must be non-negative" }
    }

    companion object {
        val ZERO = Reps(0)
    }
}
