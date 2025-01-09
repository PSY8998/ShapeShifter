package app.shapeshifter.data.db.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.Set_plan
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.db.SqlDelightTransactionRunner
import app.shapeshifter.data.db.Workout_plan
import app.shapeshifter.data.models.PositiveInt
import app.shapeshifter.data.models.plans.SetPlan
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

@Inject
class SqlDelightSetPlanEntityDao(
    override val db: ShapeShifterDatabase,
    private val transactionRunner: DatabaseTransactionRunner,
    private val dispatchers: AppCoroutineDispatchers,
) : SqlDelightEntityDao<SetPlan> {
    override fun insert(entity: SetPlan): Long {
        return transactionRunner {
            db.set_planQueries.insert(
                id = entity.id,
                exercisePlanId = entity.exercisePlanId,
                weight = entity.weight.toLong(),
                reps = entity.reps.toLong(),
            )
            db.set_planQueries.lastInsertRowId().executeAsOne()
        }
    }

    override fun update(entity: SetPlan) {
        TODO("Not yet implemented")
    }

    override fun deleteEntity(entity: SetPlan) {
        TODO("Not yet implemented")
    }

    fun observeSetPlans(): Flow<List<SetPlan>> {
        return db.set_planQueries.selectAll()
            .asFlow()
            .mapToList(dispatchers.io)
            .mapNotNull {plans ->
                plans.map { plan ->
                    SetPlan(
                        id = plan.id,
                        exercisePlanId = plan.exercise_plan_id,
                        index = PositiveInt(0),
                        weight = plan.weight.toInt(),
                        reps = plan.reps.toInt(),
                    )
                }
            }
    }

}
