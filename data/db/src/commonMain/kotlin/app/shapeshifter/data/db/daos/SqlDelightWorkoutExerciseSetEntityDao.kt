package app.shapeshifter.data.db.daos

import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.workoutlog.SetLog
import me.tatarka.inject.annotations.Inject

interface WorkoutExerciseSetEntityDao: EntityDao<SetLog>

@Inject
class SqlDelightWorkoutExerciseSetEntityDao(
    override val db: ShapeShifterDatabase,
    private val transactionRunner: DatabaseTransactionRunner
) : SqlDelightEntityDao<SetLog>, WorkoutExerciseSetEntityDao {
    override fun insert(entity: SetLog): Long {
        return transactionRunner {
            db.set_logQueries.insert(
                id = entity.id,
                exerciseLogId = entity.exerciseLogId,
                weight = entity.weight.value.toLong(),
                reps = entity.reps.value.toLong()
            )

            db.set_logQueries.lastInsertRowId().executeAsOne()
        }
    }

    override fun update(entity: SetLog) {
        TODO("Not yet implemented")
    }

    override fun deleteEntity(entity: SetLog) {
        TODO("Not yet implemented")
    }
}
