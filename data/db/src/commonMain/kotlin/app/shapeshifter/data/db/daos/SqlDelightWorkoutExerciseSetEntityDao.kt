package app.shapeshifter.data.db.daos

import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.workout.WorkoutExerciseSet
import me.tatarka.inject.annotations.Inject

interface WorkoutExerciseSetEntityDao: EntityDao<WorkoutExerciseSet>

@Inject
class SqlDelightWorkoutExerciseSetEntityDao(
    override val db: ShapeShifterDatabase,
    private val transactionRunner: DatabaseTransactionRunner
) : SqlDelightEntityDao<WorkoutExerciseSet>, WorkoutExerciseSetEntityDao {
    override fun insert(entity: WorkoutExerciseSet): Long {
        return transactionRunner {
            db.workout_exercise_setQueries.insert(
                id = entity.id,
                workoutExerciseId = entity.workoutExerciseId,
                weight = entity.weight.value.toLong(),
                reps = entity.reps.value.toLong()
            )

            db.workout_exercise_setQueries.lastInsertRowId().executeAsOne()
        }
    }

    override fun update(entity: WorkoutExerciseSet) {
        TODO("Not yet implemented")
    }

    override fun deleteEntity(entity: WorkoutExerciseSet) {
        TODO("Not yet implemented")
    }
}
