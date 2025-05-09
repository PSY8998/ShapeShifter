package app.shapeshifter.data.models

data class ExerciseTemplate(
    val id: Long,
    val name: String,
    val primaryMuscle: Muscles,
    val secondaryMuscles: List<Muscles>,
    val imageUrl: String,
)
