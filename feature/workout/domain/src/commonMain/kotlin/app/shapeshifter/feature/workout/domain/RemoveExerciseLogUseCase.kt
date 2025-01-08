package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.daos.ExerciseLogEntityDao
import app.shapeshifter.data.models.workoutlog.ExerciseLog
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.withContext

@Inject
class RemoveExerciseLogUseCase(
    private val dispatchers: AppCoroutineDispatchers,
    private val dao: ExerciseLogEntityDao,
) : UseCase<RemoveExerciseLogUseCase.Params, ExerciseLog?>() {

    override suspend fun doWork(params: Params): ExerciseLog? {
        return withContext(dispatchers.databaseWrite) {
            val exerciseSession = dao.exerciseSession(params.exerciseLogId)
            if (exerciseSession?.exerciseLog != null) {
                dao.deleteEntity(exerciseSession.exerciseLog)
            }
            exerciseSession?.exerciseLog
        }
    }

    data class Params(
        val exerciseLogId: Long,
    )
}
