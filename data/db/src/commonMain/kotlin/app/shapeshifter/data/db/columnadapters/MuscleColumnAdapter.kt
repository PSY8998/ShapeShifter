package app.shapeshifter.data.db.columnadapters

import app.cash.sqldelight.ColumnAdapter
import app.shapeshifter.data.models.Muscles

internal object MuscleColumnAdapter : ColumnAdapter<Muscles, String> {
    override fun decode(databaseValue: String): Muscles {
        return Muscles.safeValueOf(databaseValue)
    }

    override fun encode(value: Muscles): String {
        return value.name
    }
}

internal object MusclesColumnAdapter : ColumnAdapter<List<Muscles>, String> {
    override fun decode(databaseValue: String): List<Muscles> {
        if (databaseValue.isBlank()) return emptyList()
        return databaseValue.split(",").map { Muscles.safeValueOf(it) }
    }

    override fun encode(value: List<Muscles>): String {
        return value.joinToString(",")
    }
}
