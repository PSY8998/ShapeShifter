package app.shapeshifter.data.db.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.Exercise_plan
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.db.Workout_plan
import app.shapeshifter.data.models.Exercise
import app.shapeshifter.data.models.PositiveInt
import app.shapeshifter.data.models.plans.ExercisePlan
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

@Inject
class SqlDelightExercisePlanEntityDao(
    override val db: ShapeShifterDatabase,
    private val transactionRunner: DatabaseTransactionRunner,
    private val dispatchers: AppCoroutineDispatchers,
) : SqlDelightEntityDao<ExercisePlan> {
    override fun insert(entity: ExercisePlan): Long {
        return transactionRunner {
            db.exercise_planQueries.insert(
                id = entity.id,
                exerciseId = entity.exerciseId,
                workoutPlanId = entity.workoutPlanId,
            )
            db.exercise_planQueries.lastInsertRowId().executeAsOne()
        }
    }

    override fun update(entity: ExercisePlan) {
        TODO("Not yet implemented")
    }

    override fun deleteEntity(entity: ExercisePlan) {

    }

    fun observeExercisePlans(): Flow<List<Pair<ExercisePlan, Exercise>>> {
        return db.exercise_planQueries.selectAll()
            .asFlow()
            .mapToList(dispatchers.io)
            .mapNotNull { plans ->
                plans.map { plan ->
                    ExercisePlan(
                        id = plan.exercise_plan_id,
                        workoutPlanId = plan.workout_plan_id,
                        exerciseId = plan.exercise_id,
                        index = PositiveInt(0),
                    ) to
                        Exercise(
                            id = plan.exercise_plan_id,
                            name = plan.exercise_name,
                            primaryMuscle = plan.exercise_primary_muscle,
                            secondaryMuscle = plan.exercise_secondary_muscles,
                            imageUrl = plan.exercise_image_url,
                        )
                }
            }
    }
}
