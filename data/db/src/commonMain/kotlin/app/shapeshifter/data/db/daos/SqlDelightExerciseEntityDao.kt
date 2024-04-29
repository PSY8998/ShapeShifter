package app.shapeshifter.data.db.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.Exercise
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow

interface ExerciseEntityDao : EntityDao<Exercise> {
    fun observeExercises(): Flow<List<Exercise>>
}

@Inject
class SqlDelightExerciseEntityDao(
    override val db: ShapeShifterDatabase,
    private val dispatchers: AppCoroutineDispatchers,
) : SqlDelightEntityDao<Exercise>, ExerciseEntityDao {

    override fun insert(entity: Exercise) {
        db.exerciseQueries.insert(
            id = entity.id,
            name = entity.name,
            instructions = entity.instructions,
        )
    }

    override fun update(entity: Exercise) {
        TODO("Not yet implemented")
    }

    override fun deleteEntity(entity: Exercise) {
        TODO("Not yet implemented")
    }

    override fun observeExercises(): Flow<List<Exercise>> {
        return db.exerciseQueries.selectAll(
            mapper = ::Exercise,
        )
            .asFlow()
            .mapToList(dispatchers.io)
    }
}
