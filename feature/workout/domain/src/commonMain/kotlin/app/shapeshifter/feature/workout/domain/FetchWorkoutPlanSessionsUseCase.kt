package app.shapeshifter.feature.workout.domain

import app.shapeshifter.data.db.daos.SqlDelightWorkoutPlanEntityDao
import app.shapeshifter.data.models.plans.WorkoutPlanSession
import app.shapeshifter.domain.FlowUseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow

@Inject
class FetchWorkoutPlanSessionsUseCase(
    private val workoutPlanEntityDao: SqlDelightWorkoutPlanEntityDao,
) : FlowUseCase<Unit, List<WorkoutPlanSession>>() {
    override fun createObservable(params: Unit): Flow<List<WorkoutPlanSession>> {
        return workoutPlanEntityDao.observeWorkoutPlanSessions()
    }
}
