package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.daos.WorkoutEntityDao
import app.shapeshifter.data.models.workoutlog.WorkoutLog
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.withContext

@Inject
class DiscardWorkoutUseCase(
    private val dispatchers: AppCoroutineDispatchers,
    private val dao: WorkoutEntityDao,
) : UseCase<DiscardWorkoutUseCase.Params, Unit>() {
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.databaseWrite) {
            dao.deleteEntity(params.workoutLog)
        }
    }

    data class Params(
        val workoutLog: WorkoutLog,
    )
}
