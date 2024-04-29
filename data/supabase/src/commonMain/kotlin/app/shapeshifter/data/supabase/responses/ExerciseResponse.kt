package app.shapeshifter.data.supabase.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("name")
    val name: String
)
