package app.shapeshifter.data.db.daos

import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.routines.Routine
import me.tatarka.inject.annotations.Inject

interface RoutineEntityDao {
    fun insert(entity: Routine): Long
}

@Inject
class SqlDelightRoutineEntityDao(
    val db: ShapeShifterDatabase,
) : RoutineEntityDao {

    override fun insert(entity: Routine): Long {
        db.routineQueries.insert(
            id = entity.id,
            name = entity.name,
        )

        return db.routineQueries.lastInsertRowId().executeAsOne()
    }
}
