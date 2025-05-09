package app.shapeshifter.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    override val id: Long = 0,
    val name: String,
    val primaryMuscle: Muscles,
    val secondaryMuscle: List<Muscles>,
    val imageUrl: String,
) : Entity

data class ExerciseTemplate(
    val id: Long,
    val name: String,
    val primaryMuscle: Muscles,
    val secondaryMuscle: List<Muscles>,
    val imageUrl: String,
)
