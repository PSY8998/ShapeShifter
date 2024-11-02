package app.shapeshifter.feature.workout.domain

import app.shapeshifter.data.db.daos.WorkoutEntityDao
import app.shapeshifter.data.models.workoutlog.WorkoutSession
import app.shapeshifter.domain.FlowUseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow

@Inject
class ObserveWorkoutDetailsUseCase(
    private val dao: WorkoutEntityDao,
) : FlowUseCase<ObserveWorkoutDetailsUseCase.Params, WorkoutSession>() {
    override fun createObservable(params: Params): Flow<WorkoutSession> {
        return dao.observeWorkoutWithExercisesAndSets(
            workoutPlanId = params.workoutPlanId,
            workoutLogId = params.workoutLogId,
        )
    }

    data class Params(
        val workoutLogId: Long,
        val workoutPlanId: Long,
    )
}
