package app.shapeshifter.data.models

data class Exercise(
    override val id: Long = 0,
    val name: String,
    val primaryMuscle: Muscles,
    val secondaryMuscle: List<Muscles>,
    val imageUrl: String,
) : Entity
