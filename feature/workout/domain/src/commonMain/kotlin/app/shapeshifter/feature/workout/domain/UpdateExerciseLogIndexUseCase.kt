package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.daos.ExerciseLogEntityDao
import app.shapeshifter.data.models.workoutlog.ExerciseLog
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.withContext

@Inject
class UpdateExerciseLogIndexUseCase(
    private val dispatchers: AppCoroutineDispatchers,
    private val dao: ExerciseLogEntityDao,
) : UseCase<UpdateExerciseLogIndexUseCase.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        return withContext(dispatchers.databaseWrite){

        }
    }

    data class Params(
        val exerciseLogs: List<ExerciseLog>
    )
}
