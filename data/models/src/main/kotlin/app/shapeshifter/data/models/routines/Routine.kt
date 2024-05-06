package app.shapeshifter.data.models.routines

import app.shapeshifter.data.models.Entity

data class Routine(
    override val id: Long = 0,
    val name: String,
) : Entity
