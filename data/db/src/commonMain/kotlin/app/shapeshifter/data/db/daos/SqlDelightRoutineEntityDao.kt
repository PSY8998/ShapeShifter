package app.shapeshifter.data.db.daos

import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.Routine
import me.tatarka.inject.annotations.Inject

interface RoutineEntityDao : EntityDao<Routine>

@Inject
class SqlDelightRoutineEntityDao(
    override val db: ShapeShifterDatabase,
) : SqlDelightEntityDao<Routine>, RoutineEntityDao {
    override fun insert(entity: Routine) {
        db.routineQueries.insert(
            id = entity.id,
            name = entity.name,
        )
    }

    override fun update(entity: Routine) {
        TODO("Not yet implemented")
    }

    override fun deleteEntity(entity: Routine) {
        TODO("Not yet implemented")
    }
}
