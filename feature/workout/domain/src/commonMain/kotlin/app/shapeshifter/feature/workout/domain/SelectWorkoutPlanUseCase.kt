package app.shapeshifter.feature.workout.domain

import app.shapeshifter.data.db.daos.SqlDelightWorkoutPlanEntityDao
import app.shapeshifter.data.models.workout.WorkoutPlanSession
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject

@Inject
class SelectWorkoutPlanUseCase(
    private val workoutPlanEntityDao: SqlDelightWorkoutPlanEntityDao,
) : UseCase<SelectWorkoutPlanUseCase.Params, WorkoutPlanSession>() {

    override suspend fun doWork(params: Params): WorkoutPlanSession {
        return workoutPlanEntityDao.workoutPlanSession(params.workoutPlanId)
    }

    data class Params(
        val workoutPlanId: Long,
    )

}
