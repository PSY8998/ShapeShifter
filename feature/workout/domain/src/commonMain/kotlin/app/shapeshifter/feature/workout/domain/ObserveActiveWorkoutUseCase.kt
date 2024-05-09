package app.shapeshifter.feature.workout.domain

import app.shapeshifter.data.db.daos.WorkoutEntityDao
import app.shapeshifter.data.models.workoutlog.WorkoutSessionOverview
import app.shapeshifter.domain.FlowUseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow

@Inject
class ObserveActiveWorkoutUseCase(
    private val dao: WorkoutEntityDao,
) : FlowUseCase<Unit, WorkoutSessionOverview?>() {
    override fun createObservable(params: Unit): Flow<WorkoutSessionOverview?> {
        return dao.activeWorkout()
    }
}
