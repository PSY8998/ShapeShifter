package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.daos.WorkoutExerciseEntityDao
import app.shapeshifter.data.models.workout.WorkoutExercise
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.withContext

@Inject
class StoreWorkoutExerciseUseCase(
    private val dispatchers: AppCoroutineDispatchers,
    private val dao: WorkoutExerciseEntityDao,
) : UseCase<StoreWorkoutExerciseUseCase.Params, Unit>() {
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.databaseWrite) {
            val workoutExercises = params.exerciseIds.map {
                WorkoutExercise(
                    id = 0,
                    workoutId = params.workoutId,
                    exerciseId = it,
                    note = "",
                )
            }
            dao.insert(workoutExercises)
        }
    }

    data class Params(
        val workoutId: Long,
        val exerciseIds: List<Long>,
    )
}
