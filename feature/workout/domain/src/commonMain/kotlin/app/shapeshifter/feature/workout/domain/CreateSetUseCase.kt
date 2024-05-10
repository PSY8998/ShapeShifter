package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.daos.WorkoutExerciseSetEntityDao
import app.shapeshifter.data.models.PositiveInt
import app.shapeshifter.data.models.workoutlog.SetLog
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.withContext

@Inject
class CreateSetUseCase(
    private val dispatchers: AppCoroutineDispatchers,
    private val dao : WorkoutExerciseSetEntityDao
) : UseCase<CreateSetUseCase.Params, Long>() {
    override suspend fun doWork(params: Params): Long {
        val setLog = SetLog(
            id = 0L,
            workoutExerciseId = params.workoutExerciseId ,
            index = PositiveInt(1),
            weight = PositiveInt(0),
            reps = PositiveInt(0),
            completed = false
        )
        return withContext(dispatchers.databaseWrite){
            dao.insert(setLog)
        }
    }

    data class Params(
        val workoutExerciseId: Long
    )

}
