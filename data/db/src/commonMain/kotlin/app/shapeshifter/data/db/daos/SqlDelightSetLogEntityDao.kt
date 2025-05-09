package app.shapeshifter.data.db.daos

import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.workout.SetLog
import me.tatarka.inject.annotations.Inject

interface SetLogEntityDao {
    fun insert(entity: SetLog): Long
    fun insert(entities: List<SetLog>)
    fun upsert(entity: SetLog): Long
    fun update(entity: SetLog)
    fun deleteEntity(entity: SetLog)
}

@Inject
class SqlDelightSetLogEntityDao(
    val db: ShapeShifterDatabase,
    private val transactionRunner: DatabaseTransactionRunner,
) : SetLogEntityDao {

    override fun insert(entities: List<SetLog>) {
        db.transaction {
            for (entity in entities) {
                insert(entity)
            }
        }
    }

    override fun insert(entity: SetLog): Long {
        val lastSet = db.set_logQueries.exerciseLastSet(
            exerciseLogId = entity.exerciseId,
        ).executeAsOneOrNull()

        // update set index
        val setIndex = lastSet?.set_index?.toInt()?.let { it + 1 } ?: 0

        return transactionRunner {
            db.set_logQueries.insert(
                id = entity.id,
                exerciseLogId = entity.exerciseId,
                setIndex = setIndex.toLong(),
                weight = entity.weight.value.toLong(),
                reps = entity.reps.value.toLong(),
                setTypeId = entity.setTypeId.toLong(),
                isCompleted = if (entity.isCompleted) 1 else 0,
            )

            db.set_logQueries.lastInsertRowId().executeAsOne()
        }
    }

    override fun upsert(entity: SetLog): Long {
        return if (entity.id != 0L) {
            update(entity)
            entity.id
        } else {
            insert(entity)
        }
    }

    override fun update(entity: SetLog) {
        db.set_logQueries.update(
            exerciseLogId = entity.exerciseId,
            weight = entity.weight.value.toLong(),
            reps = entity.reps.value.toLong(),
            isCompleted = if (entity.isCompleted) 1 else 0,
            setTypeId = entity.setTypeId.toLong(),
            id = entity.id,
        )
    }

    override fun deleteEntity(entity: SetLog) {
        db.set_logQueries.delete(entity.id)
    }
}
