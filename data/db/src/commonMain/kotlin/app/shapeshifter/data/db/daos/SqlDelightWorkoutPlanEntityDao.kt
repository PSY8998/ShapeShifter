package app.shapeshifter.data.db.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.db.Workout_plan
import app.shapeshifter.data.models.Exercise
import app.shapeshifter.data.models.plans.WorkoutPlan
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

@Inject
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

    fun observeWorkoutPlans(): Flow<List<WorkoutPlan>> {
        return db.workout_planQueries.selectAll()
            .asFlow()
            .mapToList(dispatchers.io)
            .mapNotNull { plans->
                plans.map { plan->
                    WorkoutPlan(
                        id = plan.id,
                        routineId = plan.routine_id,
                        name = plan.name,
                    )
                }
            }
    }

}
