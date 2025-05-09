package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.daos.ExerciseLogEntityDao
import app.shapeshifter.data.db.daos.SetLogEntityDao
import app.shapeshifter.data.models.workout.ExerciseLog
import app.shapeshifter.data.models.workout.Reps
import app.shapeshifter.data.models.workout.SetLog
import app.shapeshifter.data.models.workout.Weight
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.milliseconds
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
            var currentIndex = params.index
            transactionRunner {
                for (exerciseId in params.exerciseIds) {
                    val exerciseLog = ExerciseLog(
                        id = 0,
                        index = currentIndex.toInt(),
                        workoutId = params.workoutLogId,
                        exerciseTemplateId = exerciseId,
                        exercisePlanId = null,
                        note = "",
                        restDuration = 120.milliseconds,
                    )
                    val exerciseLogId = exerciseLogEntityDao.insert(exerciseLog)

                    val set = SetLog(
                        id = 0L,
                        weight = Weight.ZERO,
                        reps = Reps.ZERO,
                        exerciseId = exerciseLogId,
                        setTypeId = 2,
                        index = 0,
                        setPlanId = null,
                        isCompleted = false,
                        previousWeight = Weight.ZERO,
                        previousReps = Reps.ZERO,
                    )

                    setLogEntityDao.insert(set)
                    currentIndex += 1
                }
            }
        }

    }

    data class Params(
        val workoutLogId: Long,
        val workoutPlanId: Long?,
        val exerciseIds: List<Long>,
        val index: Long,
    )
}
