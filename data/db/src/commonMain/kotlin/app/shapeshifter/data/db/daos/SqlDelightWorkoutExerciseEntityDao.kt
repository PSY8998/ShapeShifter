package app.shapeshifter.data.db.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.workout.WorkoutExercise
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow

interface WorkoutExerciseEntityDao : EntityDao<WorkoutExercise> {
    fun observeWorkoutExercises(workoutId: Long): Flow<List<WorkoutExercise>>
}

@Inject
class SqlDelightWorkoutExerciseEntityDao(
    override val db: ShapeShifterDatabase,
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
) : SqlDelightEntityDao<WorkoutExercise>, WorkoutExerciseEntityDao {
    override fun insert(entity: WorkoutExercise): Long {
        db.workout_exerciseQueries.insert(
            id = entity.id,
            exercise_id = entity.exerciseId,
            workout_id = entity.workoutId,
        )

        return db.workout_exerciseQueries.lastInsertRowId().executeAsOne()
    }

    override fun update(entity: WorkoutExercise) {
        TODO("Not yet implemented")
    }

    override fun deleteEntity(entity: WorkoutExercise) {
        TODO("Not yet implemented")
    }

    override fun observeWorkoutExercises(workoutId: Long): Flow<List<WorkoutExercise>> {
        return db
            .workout_exerciseQueries
            .selectAll(
                workout_id = workoutId,
                mapper = { id, exerciseId, wId ->
                    WorkoutExercise(
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
