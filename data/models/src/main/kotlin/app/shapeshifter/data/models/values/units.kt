package app.shapeshifter.data.models.values

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
