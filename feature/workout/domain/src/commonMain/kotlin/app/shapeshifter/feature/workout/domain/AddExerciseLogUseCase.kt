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
import kotlin.time.Duration.Companion.seconds
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
                        index = currentIndex,
                        workoutLogId = params.workoutLogId,
                        workoutPlanId = params.workoutPlanId,
                        exerciseId = exerciseId,
                        exercisePlanId = null,
                        note = "",
                        restTimeDuration = 120.seconds.inWholeMilliseconds,
                    )
                    val exerciseLogId = exerciseLogEntityDao.insert(exerciseLog)

                    val set = SetLog(
                        id = 0L,
                        exerciseLogId = exerciseLogId,
                        setIndex = PositiveInt(0),
                        weight = PositiveInt(0),
                        reps = PositiveInt(0),
                        prevReps = PositiveInt(0),
                        prevWeight = PositiveInt(0),
                        completed = false,
                        finishTime = 0,
                        exercisePlanId = null,
                        exerciseId = exerciseId,
                        workoutPlanId = params.workoutPlanId,
                        workoutLogId = params.workoutLogId,
                    )

                    setLogEntityDao.insert(set)
                    currentIndex += 1
                }
            }
        }

    }

    data class Params(
        val workoutLogId: Long,
        val workoutPlanId: Long,
        val exerciseIds: List<Long>,
        val index: Long,
    )
}
