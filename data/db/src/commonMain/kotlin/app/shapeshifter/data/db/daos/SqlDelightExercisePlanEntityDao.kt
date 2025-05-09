package app.shapeshifter.data.db.daos

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.workout.ExercisePlan
import me.tatarka.inject.annotations.Inject

@Inject
class SqlDelightExercisePlanEntityDao(
    val db: ShapeShifterDatabase,
    private val transactionRunner: DatabaseTransactionRunner,
    private val dispatchers: AppCoroutineDispatchers,
) {

    fun updateOrInsert(entity: ExercisePlan): Long {
        return if (entity.id == 0L) {
            insert(entity)
        } else {
            update(entity)
            entity.id
        }
    }

    fun insert(entity: ExercisePlan): Long {
        return transactionRunner {
            db.exercise_planQueries.insert(
                id = entity.id,
                exerciseTemplateId = entity.exerciseTemplateId,
                workoutPlanId = entity.workoutId,
            )
            db.exercise_planQueries.lastInsertRowId().executeAsOne()
        }
    }

    fun update(entity: ExercisePlan) {
        db.exercise_planQueries.update(
            exerciseTemplateId = entity.exerciseTemplateId,
            workoutPlanId = entity.workoutId,
            id = entity.id,
        )
    }
}
