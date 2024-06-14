package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.daos.ExerciseLogEntityDao
import app.shapeshifter.data.db.daos.SetLogEntityDao
import app.shapeshifter.data.models.PositiveInt
import app.shapeshifter.data.models.workoutlog.ExerciseLog
import app.shapeshifter.data.models.workoutlog.SetLog
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.withContext

@Inject
class AddExerciseLogUseCase(
    private val dispatchers: AppCoroutineDispatchers,
    private val transactionRunner: DatabaseTransactionRunner,
    private val exerciseLogEntityDao: ExerciseLogEntityDao,
    private val setLogEntityDao: SetLogEntityDao,
) : UseCase<AddExerciseLogUseCase.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.databaseWrite) {
            transactionRunner {
                for (exerciseId in params.exerciseIds) {
                    val exerciseSession = exerciseLogEntityDao.exerciseSession(exerciseId)
                    val exerciseLog = ExerciseLog(
                        id = 0,
                        workoutId = params.workoutId,
                        exerciseId = exerciseId,
                        note = "",
                    )
                    val exerciseLogId = exerciseLogEntityDao.insert(exerciseLog)

                    val sets = exerciseSession?.sets?.map { setLog ->
                        SetLog(
                            id = 0L,
                            exerciseLogId = exerciseLogId,
                            index = PositiveInt(1),
                            weight = PositiveInt(0),
                            reps = PositiveInt(0),
                            prevReps = setLog.reps,
                            prevWeight = setLog.weight,
                            completed = false,
                            finishTime = 0,
                        )
                    } ?: listOf(
                        SetLog(
                            id = 0L,
                            exerciseLogId = exerciseLogId,
                            index = PositiveInt(1),
                            weight = PositiveInt(0),
                            reps = PositiveInt(0),
                            prevReps = PositiveInt(0),
                            prevWeight = PositiveInt(0),
                            completed = false,
                            finishTime = 0,
                        )
                    )

                    setLogEntityDao.insert(sets)
                }
            }
        }

    }

    data class Params(
        val workoutId: Long,
        val exerciseIds: List<Long>,
    )
}
