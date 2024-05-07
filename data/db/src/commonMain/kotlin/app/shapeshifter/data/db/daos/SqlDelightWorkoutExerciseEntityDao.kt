package app.shapeshifter.data.db.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.workoutlog.ExerciseLog
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow

interface WorkoutExerciseEntityDao : EntityDao<ExerciseLog> {
    fun observeWorkoutExercises(workoutId: Long): Flow<List<ExerciseLog>>
}

@Inject
class SqlDelightWorkoutExerciseEntityDao(
    override val db: ShapeShifterDatabase,
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
) : SqlDelightEntityDao<ExerciseLog>, WorkoutExerciseEntityDao {
    override fun insert(entity: ExerciseLog): Long {
        db.exercise_logQueries.insert(
            id = entity.id,
            exercise_id = entity.exerciseId,
            workout_id = entity.workoutId,
        )

        return db.exercise_logQueries.lastInsertRowId().executeAsOne()
    }

    override fun update(entity: ExerciseLog) {
        TODO("Not yet implemented")
    }

    override fun deleteEntity(entity: ExerciseLog) {
        TODO("Not yet implemented")
    }

    override fun observeWorkoutExercises(workoutId: Long): Flow<List<ExerciseLog>> {
        return db
            .exercise_logQueries
            .selectAll(
                workout_id = workoutId,
                mapper = { id, exerciseId, wId ->
                    ExerciseLog(
                        id = id,
                        exerciseId = exerciseId,
                        workoutId = wId,
                        note = "",
                    )
                },
            )
            .asFlow()
            .mapToList(appCoroutineDispatchers.io)
    }
}
