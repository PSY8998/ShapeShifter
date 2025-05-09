package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.daos.ExerciseLogEntityDao
import app.shapeshifter.data.models.workout.ExerciseLog
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.withContext

@Inject
class UpdateRestTimeUseCase(
    private val dispatchers: AppCoroutineDispatchers,
    private val dao: ExerciseLogEntityDao,
) : UseCase<UpdateRestTimeUseCase.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        return withContext(dispatchers.databaseWrite) {
            dao.update(
                params.exerciseLog.copy(
                    restDuration = (params.restTimeDurationInSecs * 1000).milliseconds,
                ),
            )
        }
    }

    data class Params(
        val restTimeDurationInSecs: Long,
        val exerciseLog: ExerciseLog,
    )
}
