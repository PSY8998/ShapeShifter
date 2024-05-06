package app.shapeshifter.data.supabase.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("name")
    val name: String,

    @SerialName("primary_muscle")
    val primaryMuscle: String,

    @SerialName("secondary_muscles")
    val secondaryMuscles: List<String>?,

    @SerialName("image_url")
    val imageUrl: String?
)
