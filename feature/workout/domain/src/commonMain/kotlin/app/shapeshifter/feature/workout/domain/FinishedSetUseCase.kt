package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.daos.SetLogEntityDao
import app.shapeshifter.data.db.daos.WorkoutEntityDao
import app.shapeshifter.data.models.workout.SetLog
import app.shapeshifter.data.models.workout.WorkoutLog
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.withContext

@Inject
class FinishedSetUseCase(
    private val dispatchers: AppCoroutineDispatchers,
    private val dao: SetLogEntityDao,
    private val workoutEntityDao: WorkoutEntityDao,
) : UseCase<FinishedSetUseCase.Params, Unit>() {
    override suspend fun doWork(params: Params) {

        return withContext(dispatchers.databaseWrite) {
            dao.upsert(params.setLog)
            workoutEntityDao.update(params.workoutLog)
        }
    }

    data class Params(
        val setLog: SetLog,
        val workoutLog: WorkoutLog,
    )
}
