package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.daos.SetLogEntityDao
import app.shapeshifter.data.models.workoutlog.SetLog
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.withContext

@Inject
class DeleteSetUseCase(
    private val dispatchers: AppCoroutineDispatchers,
    private val dao: SetLogEntityDao,
) : UseCase<DeleteSetUseCase.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        return withContext(dispatchers.databaseWrite) {
            dao.deleteEntity(params.setLog)
        }
    }

    data class Params(
        val setLog: SetLog,
    )

}
