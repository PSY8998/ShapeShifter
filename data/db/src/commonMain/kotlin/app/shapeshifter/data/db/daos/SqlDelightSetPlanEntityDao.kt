package app.shapeshifter.data.db.daos

import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.workout.SetPlan
import me.tatarka.inject.annotations.Inject

@Inject
class SqlDelightSetPlanEntityDao(
    val db: ShapeShifterDatabase,
    private val transactionRunner: DatabaseTransactionRunner,
) {

    fun updateOrInsert(entity: SetPlan): Long {
        return if (entity.id == 0L) {
            insert(entity)
        } else {
            update(entity)
            entity.id
        }
    }

    fun insert(entity: SetPlan): Long {
        return transactionRunner {
            db.set_planQueries.insert(
                id = entity.id,
                exercisePlanId = entity.exerciseId,
                weight = entity.weight.value.toLong(),
                reps = entity.reps.value.toLong(),
            )
            db.set_planQueries.lastInsertRowId().executeAsOne()
        }
    }

    fun update(entity: SetPlan) {
        db.set_planQueries.update(
            weight = entity.weight.value.toLong(),
            reps = entity.reps.value.toLong(),
            id = entity.id,
        )
    }
}
