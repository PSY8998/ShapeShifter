package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.daos.ExerciseLogEntityDao
import app.shapeshifter.data.db.daos.SetLogEntityDao
import app.shapeshifter.data.db.daos.WorkoutEntityDao
import app.shapeshifter.data.models.workout.ExerciseLog
import app.shapeshifter.data.models.workout.Reps
import app.shapeshifter.data.models.workout.SetLog
import app.shapeshifter.data.models.workout.Weight
import app.shapeshifter.data.models.workout.WorkoutLog
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlin.time.Clock
import kotlin.time.Duration
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

@Inject
class CreateWorkoutUseCase(
    private val workoutEntityDao: WorkoutEntityDao,
    private val exerciseLogEntityDao: ExerciseLogEntityDao,
    private val setLogEntityDao: SetLogEntityDao,
    private val selectWorkoutPlanUseCase: SelectWorkoutPlanUseCase,
    private val dispatchers: AppCoroutineDispatchers,
    private val transactionRunner: DatabaseTransactionRunner,
) : UseCase<CreateWorkoutUseCase.Params, Long>() {

    override suspend fun doWork(params: Params): Long = withContext(dispatchers.databaseRead) {
        val activeWorkout = workoutEntityDao.activeWorkout().firstOrNull()
        if (activeWorkout != null) {
            return@withContext activeWorkout.workout.id
        }

        val startTime = Clock.System.now()

        if (params.workoutPlanId == null) {
            return@withContext withContext(dispatchers.databaseWrite) {
                workoutEntityDao.insert(
                    WorkoutLog.empty(
                        name = "Quick Workout",
                        startTime = startTime,
                    ),
                )
            }
        } else {
            val workoutPlanSession = selectWorkoutPlanUseCase(
                SelectWorkoutPlanUseCase.Params(params.workoutPlanId),
            ).getOrNull() ?: return@withContext 0

            // working only with transactionRunner otherwise exerciseLogId always 0
            return@withContext transactionRunner {
                val workoutLog = WorkoutLog(
                    id = 0,
                    workoutPlanId = workoutPlanSession.workout.id,
                    name = workoutPlanSession.workout.name,
                    startTime = startTime,
                    finishTime = null,
                    note = "",
                )

                val workoutLogId = workoutEntityDao.insert(workoutLog)

                workoutPlanSession.exerciseSessions.forEach { exercisePlanSession ->
                    val exerciseLog = ExerciseLog(
                        id = 0,
                        index = 0,
                        exercisePlanId = exercisePlanSession.exercise.id,
                        note = "",
                        workoutId = workoutLogId,
                        exerciseTemplateId = exercisePlanSession.exercise.exerciseTemplateId,
                        restDuration = Duration.ZERO,
                    )

                    val exerciseLogId =
                        try {
                            exerciseLogEntityDao.insert(exerciseLog)
                        } catch (e: Exception) {
                            0
                        }

                    val setLogs = exercisePlanSession.sets.map { setPlan ->
                        SetLog(
                            id = 0,
                            exerciseId = exerciseLogId,
                            weight = setPlan.weight,
                            reps = setPlan.reps,
                            setTypeId = 2,
                            index = setPlan.index,
                            setPlanId = null,
                            isCompleted = false,
                            previousWeight = Weight.ZERO,
                            previousReps = Reps.ZERO,
                        )
                    }
                    setLogEntityDao.insert(setLogs)
                }

                workoutLogId
            }
        }
    }

    data class Params(
        val workoutPlanId: Long?,
    )
}
