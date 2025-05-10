package app.shapeshifter.feature.workout.domain

import app.shapeshifter.data.db.daos.SqlDelightExercisePlanEntityDao
import app.shapeshifter.data.db.daos.SqlDelightSetPlanEntityDao
import app.shapeshifter.data.db.daos.SqlDelightWorkoutPlanEntityDao
import app.shapeshifter.data.models.workout.WorkoutPlanSession
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject

@Inject
class SaveWorkoutUseCase(
    private val workoutPlanEntityDao: SqlDelightWorkoutPlanEntityDao,
    private val exercisePlanEntityDao: SqlDelightExercisePlanEntityDao,
    private val setPlanEntityDao: SqlDelightSetPlanEntityDao,
) : UseCase<WorkoutPlanSession, Unit>() {
    override suspend fun doWork(params: WorkoutPlanSession) {
        val workoutPlan = params.workout

        val workoutId = workoutPlanEntityDao.updateOrInsert(
            entity = workoutPlan,
        )

        for (exercisePlanSession in params.exerciseSessions) {
            val exercisePlan = exercisePlanSession.exercise
            val exercisePlanId = exercisePlanEntityDao.updateOrInsert(
                entity = exercisePlan.copy(
                    workoutId = workoutId,
                    id = exercisePlan.id.takeIf { it > 0 } ?: 0,
                ),
            )
            for (setPlan in exercisePlanSession.sets) {
                setPlanEntityDao.updateOrInsert(
                    entity = setPlan.copy(
                        id = setPlan.id.takeIf { it > 0 } ?: 0,
                        exerciseId = exercisePlanId,
                    ),
                )
            }
        }
    }

}
