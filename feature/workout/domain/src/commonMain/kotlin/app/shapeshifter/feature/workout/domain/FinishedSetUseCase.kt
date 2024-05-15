package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.daos.SetLogEntityDao
import app.shapeshifter.data.models.PositiveInt
import app.shapeshifter.data.models.workoutlog.SetLog
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.withContext

@Inject
class FinishedSetUseCase(
    private val dispatchers: AppCoroutineDispatchers,
    private val dao: SetLogEntityDao,
) : UseCase<FinishedSetUseCase.Params, Long>() {
    override suspend fun doWork(params: Params): Long {

        val setLog = params.setLog
            .copy(
                finishTime = if (params.isCompleted) System.currentTimeMillis()
                else 0,
            )

        return withContext(dispatchers.databaseWrite) {
            dao.upsert(setLog)
        }
    }

    data class Params(
        val setLog: SetLog,
        val isCompleted: Boolean,
    )
}
