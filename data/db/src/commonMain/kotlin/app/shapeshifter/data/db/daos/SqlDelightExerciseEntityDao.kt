package app.shapeshifter.data.db.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.Exercise
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow

interface ExerciseEntityDao : EntityDao<Exercise> {
    fun observeExercises(): Flow<List<Exercise>>
    fun select(ids: List<Long>): List<Exercise>
}

@Inject
class SqlDelightExerciseEntityDao(
    override val db: ShapeShifterDatabase,
    private val transactionRunner: DatabaseTransactionRunner,
    private val dispatchers: AppCoroutineDispatchers,
) : SqlDelightEntityDao<Exercise>, ExerciseEntityDao {

    override fun insert(entity: Exercise): Long {
        return transactionRunner {
            db.exerciseQueries.insert(
                id = entity.id,
                name = entity.name,
                primary_muscle = entity.primaryMuscle,
                secondary_muscles = entity.secondaryMuscle,
                image_url = entity.imageUrl,
            )
            db.exerciseQueries.lastInsertRowId().executeAsOne()
        }
    }

    override fun update(entity: Exercise) {
        TODO("Not yet implemented")
    }

    override fun deleteEntity(entity: Exercise) {
        TODO("Not yet implemented")
    }

    override fun observeExercises(): Flow<List<Exercise>> {
        return db.exerciseQueries.selectAll(
            mapper = { id, name, primary_muscle, secondary_muscle, image_url ->
                Exercise(
                    id = id,
                    name = name,
                    primaryMuscle = primary_muscle,
                    secondaryMuscle = secondary_muscle,
                    imageUrl = image_url,
                )
            },
        )
            .asFlow()
            .mapToList(dispatchers.io)
    }

    override fun select(ids: List<Long>): List<Exercise> {
        return db.exerciseQueries.select(
            ids = ids,
            mapper = { id, name, primary_muscle, secondary_muscle, image_url ->
                Exercise(
                    id = id,
                    name = name,
                    primaryMuscle = primary_muscle,
                    secondaryMuscle = secondary_muscle,
                    imageUrl = image_url
                )
            }
        ).executeAsList()
    }
}
