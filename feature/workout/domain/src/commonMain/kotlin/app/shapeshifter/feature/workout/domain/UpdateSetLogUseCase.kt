package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.daos.SetLogEntityDao
import app.shapeshifter.data.models.workout.SetLog
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.withContext

@Inject
class UpdateSetLogUseCase(
    private val dispatchers: AppCoroutineDispatchers,
    private val dao: SetLogEntityDao,
) : UseCase<UpdateSetLogUseCase.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.databaseWrite) {
            val updatedSetLog = params.setLog.copy(
                weight = params.setLog.weight,
                reps = params.setLog.reps,
                isCompleted = params.setLog.isCompleted,
                setTypeId = params.setLog.setTypeId,
            )

            dao.update(updatedSetLog)
        }
    }

    data class Params(
        val setLog: SetLog,
    )
}
