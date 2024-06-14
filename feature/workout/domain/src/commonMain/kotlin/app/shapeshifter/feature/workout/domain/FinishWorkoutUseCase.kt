package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.daos.WorkoutEntityDao
import app.shapeshifter.data.models.workoutlog.WorkoutLog
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.withContext

@Inject
class FinishWorkoutUseCase(
    private val dispatchers: AppCoroutineDispatchers,
    private val dao: WorkoutEntityDao,
) : UseCase<FinishWorkoutUseCase.Params, Long>() {
    override suspend fun doWork(params: Params): Long {

        return withContext(dispatchers.databaseWrite) {
            dao.upsert(params.workoutLog.copy(finishTimeInMillis = System.currentTimeMillis()))
        }
    }

    data class Params(
        val workoutLog: WorkoutLog,
    )
}
