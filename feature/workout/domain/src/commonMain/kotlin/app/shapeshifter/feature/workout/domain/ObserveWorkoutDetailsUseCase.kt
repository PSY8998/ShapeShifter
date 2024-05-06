package app.shapeshifter.feature.workout.domain

import app.shapeshifter.data.db.daos.WorkoutEntityDao
import app.shapeshifter.data.models.workout.WorkoutWithExercisesAndSets
import app.shapeshifter.domain.FlowUseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Inject
class ObserveWorkoutDetailsUseCase(
    private val dao: WorkoutEntityDao,
) : FlowUseCase<ObserveWorkoutDetailsUseCase.Params, WorkoutWithExercisesAndSets>() {
    override fun createObservable(params: Params): Flow<WorkoutWithExercisesAndSets> {
        return dao.observeWorkoutWithExercisesAndSets(params.workoutId)
    }

    data class Params(
        val workoutId: Long,
    )
}
