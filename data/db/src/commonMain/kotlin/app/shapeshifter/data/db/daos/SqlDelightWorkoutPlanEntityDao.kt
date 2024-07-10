package app.shapeshifter.data.db.daos

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.plans.WorkoutPlan

class SqlDelightWorkoutPlanEntityDao(
    override val db: ShapeShifterDatabase,
    private val transactionRunner: DatabaseTransactionRunner,
    private val dispatchers: AppCoroutineDispatchers
) : SqlDelightEntityDao<WorkoutPlan> {
    override fun insert(entity: WorkoutPlan): Long {
        return transactionRunner{
            db.workout_planQueries.insert(
                id = entity.id,
                routineId = entity.routineId,
                name = entity.name,
            )
            db.workout_planQueries.lastInsertRowId().executeAsOne()
        }
    }

    override fun update(entity: WorkoutPlan) {
        TODO("Not yet implemented")
    }

    override fun deleteEntity(entity: WorkoutPlan) {
        TODO("Not yet implemented")
    }

}
