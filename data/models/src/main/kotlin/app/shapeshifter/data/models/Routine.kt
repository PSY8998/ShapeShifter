package app.shapeshifter.data.models

data class Routine(
    override val id: Long = 0,
    val name: String,
) : Entity
