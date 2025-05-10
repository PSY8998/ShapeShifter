package app.shapeshifter.feature.workout.domain

import app.shapeshifter.data.db.daos.WorkoutEntityDao
import app.shapeshifter.data.models.workout.WorkoutLogSession
import app.shapeshifter.domain.FlowUseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow

@Inject
class ObserveWorkoutDetailsUseCase(
    private val dao: WorkoutEntityDao,
) : FlowUseCase<ObserveWorkoutDetailsUseCase.Params, WorkoutLogSession>() {
    override fun createObservable(params: Params): Flow<WorkoutLogSession> {
        return dao.observeWorkoutWithExercisesAndSets(
            workoutLogId = params.workoutLogId,
        )
    }

    data class Params(
        val workoutLogId: Long,
    )
}
