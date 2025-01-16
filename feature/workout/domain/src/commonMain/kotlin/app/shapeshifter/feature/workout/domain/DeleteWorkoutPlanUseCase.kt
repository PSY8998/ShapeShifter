package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.daos.SqlDelightWorkoutPlanEntityDao
import app.shapeshifter.data.models.plans.WorkoutPlanSession
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.withContext

@Inject
class DeleteWorkoutPlanUseCase(
    private val dispatchers: AppCoroutineDispatchers,
    private val workoutPlanEntityDao: SqlDelightWorkoutPlanEntityDao,
) : UseCase<DeleteWorkoutPlanUseCase.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        return withContext(dispatchers.databaseWrite){
            workoutPlanEntityDao.deleteEntity(params.workoutPlanSession.workoutPlan)
        }
    }

    data class Params(
        val workoutPlanSession: WorkoutPlanSession,
    )

}
