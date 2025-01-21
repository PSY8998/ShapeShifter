package app.shapeshifter.feature.workout.domain

import app.shapeshifter.data.db.daos.SqlDelightExercisePlanEntityDao
import app.shapeshifter.data.db.daos.SqlDelightSetPlanEntityDao
import app.shapeshifter.data.db.daos.SqlDelightWorkoutPlanEntityDao
import app.shapeshifter.data.models.plans.WorkoutPlanSession
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject

@Inject
class SaveWorkoutUseCase(
    private val workoutPlanEntityDao: SqlDelightWorkoutPlanEntityDao,
    private val exercisePlanEntityDao: SqlDelightExercisePlanEntityDao,
    private val setPlanEntityDao: SqlDelightSetPlanEntityDao,
) : UseCase<WorkoutPlanSession, Unit>() {
    override suspend fun doWork(params: WorkoutPlanSession) {
        val workoutPlan = params.workoutPlan

        val workoutId = workoutPlanEntityDao.updateOrInsert(
            entity = workoutPlan,
        )

        for (exercisePlanSession in params.exercisePlanSessions) {
            val exercisePlan = exercisePlanSession.exercisePlan
            val exercisePlanId = exercisePlanEntityDao.updateOrInsert(
                entity = exercisePlan.copy(
                    workoutPlanId = workoutId,
                    id = exercisePlan.id.takeIf { it > 0 } ?: 0,
                ),
            )
            for (setPlan in exercisePlanSession.setPlans) {
                setPlanEntityDao.updateOrInsert(
                    entity = setPlan.copy(
                        id = setPlan.id.takeIf { it > 0 } ?: 0,
                        exercisePlanId = exercisePlanId,
                    ),
                )
            }
        }
    }

}
