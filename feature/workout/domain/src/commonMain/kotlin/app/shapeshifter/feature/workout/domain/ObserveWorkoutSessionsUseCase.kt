package app.shapeshifter.feature.workout.domain

import app.shapeshifter.data.db.daos.WorkoutEntityDao
import app.shapeshifter.data.models.workoutlog.WorkoutSession
import app.shapeshifter.domain.FlowUseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow

@Inject
class ObserveWorkoutSessionsUseCase(
    private val workoutEntityDao: WorkoutEntityDao,
) : FlowUseCase<Unit, List<WorkoutSession>>() {
    override fun createObservable(params: Unit): Flow<List<WorkoutSession>> {
        return workoutEntityDao.observeWorkoutSessions()
    }

}
