package app.shapeshifter.data.db.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.ExerciseTemplate
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow

interface ExerciseEntityDao {
    fun insert(entity: ExerciseTemplate): Long
    fun insert(entities: List<ExerciseTemplate>)
    fun observeExercises(): Flow<List<ExerciseTemplate>>
    fun select(ids: List<Long>): List<ExerciseTemplate>
}

@Inject
class SqlDelightExerciseEntityDao(
    val db: ShapeShifterDatabase,
    private val transactionRunner: DatabaseTransactionRunner,
    private val dispatchers: AppCoroutineDispatchers,
) : ExerciseEntityDao {

    override fun insert(entity: ExerciseTemplate): Long {
        return transactionRunner {
            db.exercise_templateQueries.insert(
                id = entity.id,
                name = entity.name,
                primary_muscle = entity.primaryMuscle,
                secondary_muscles = entity.secondaryMuscles,
                image_url = entity.imageUrl,
            )
            db.exercise_templateQueries.lastInsertRowId().executeAsOne()
        }
    }

    override fun insert(entities: List<ExerciseTemplate>) {
        db.transaction {
            for (entity in entities) {
                insert(entity)
            }
        }
    }

    override fun observeExercises(): Flow<List<ExerciseTemplate>> {
        return db.exercise_templateQueries.selectAll(
            mapper = { id, name, primaryMuscle, secondaryMuscles, imageUrl ->
                ExerciseTemplate(
                    id = id,
                    name = name,
                    primaryMuscle = primaryMuscle,
                    secondaryMuscles = secondaryMuscles,
                    imageUrl = imageUrl,
                )
            },
        )
            .asFlow()
            .mapToList(dispatchers.io)
    }

    override fun select(ids: List<Long>): List<ExerciseTemplate> {
        return db.exercise_templateQueries.select(
            ids = ids,
            mapper = { id, name, primaryMuscle, secondaryMuscles, imageUrl ->
                ExerciseTemplate(
                    id = id,
                    name = name,
                    primaryMuscle = primaryMuscle,
                    secondaryMuscles = secondaryMuscles,
                    imageUrl = imageUrl,
                )
            },
        ).executeAsList()
    }
}
