package app.shapeshifter.data.db.daos

import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.workoutlog.SetLog
import me.tatarka.inject.annotations.Inject

interface SetLogEntityDao: EntityDao<SetLog>

@Inject
class SqlDelightSetLogEntityDao(
    override val db: ShapeShifterDatabase,
    private val transactionRunner: DatabaseTransactionRunner
) : SqlDelightEntityDao<SetLog>, SetLogEntityDao {
    override fun insert(entity: SetLog): Long {
        return transactionRunner {
            db.set_logQueries.insert(
                id = entity.id,
                exerciseLogId = entity.exerciseLogId,
                weight = entity.weight.value.toLong(),
                reps = entity.reps.value.toLong(),
                finishTime = entity.finishTime
            )

            db.set_logQueries.lastInsertRowId().executeAsOne()
        }
    }

    override fun update(entity: SetLog) {
        db.set_logQueries.update(
            exerciseLogId = entity.exerciseLogId,
            weight = entity.weight.value.toLong(),
            reps = entity.reps.value.toLong(),
            finishTime = entity.finishTime,
            id = entity.id
        )
    }

    override fun deleteEntity(entity: SetLog) {
        db.set_logQueries.delete(entity.id)
    }
}
