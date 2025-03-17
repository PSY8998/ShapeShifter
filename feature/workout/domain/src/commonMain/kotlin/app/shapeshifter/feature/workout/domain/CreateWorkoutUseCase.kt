package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.daos.ExerciseEntityDao
import app.shapeshifter.data.db.daos.ExerciseLogEntityDao
import app.shapeshifter.data.db.daos.SetLogEntityDao
import app.shapeshifter.data.db.daos.WorkoutEntityDao
import app.shapeshifter.data.db.daos.insert
import app.shapeshifter.data.models.PositiveInt
import app.shapeshifter.data.models.plans.WorkoutPlanSession
import app.shapeshifter.data.models.workoutlog.ExerciseLog
import app.shapeshifter.data.models.workoutlog.ExerciseSession
import app.shapeshifter.data.models.workoutlog.SetLog
import app.shapeshifter.data.models.workoutlog.WorkoutLog
import app.shapeshifter.data.models.workoutlog.WorkoutSession
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import java.util.logging.Logger
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

        if (params.workoutPlanId == -1L) {
            return@withContext withContext(dispatchers.databaseWrite) {
                workoutEntityDao.insert(WorkoutLog.emptyQuickWorkout())
            }
        } else {
            val workoutPlanSession = selectWorkoutPlanUseCase(
                SelectWorkoutPlanUseCase.Params(params.workoutPlanId),
            ).getOrNull() ?: return@withContext 0

            // working only with transactionRunner otherwise exerciseLogId always 0
            return@withContext transactionRunner {
                val workoutLog = WorkoutLog(
                    id = 0,
                    workoutPlanId = workoutPlanSession.workoutPlan.id,
                    name = workoutPlanSession.workoutPlan.name,
                    startTimeInMillis = System.currentTimeMillis(),
                    finishTimeInMillis = 0L,
                    note = "",
                    restFinishTimeInMillis = 0L,
                )

                val workoutLogId = workoutEntityDao.insert(workoutLog)

                workoutPlanSession.exercisePlanSessions.forEach { exercisePlanSession ->
                    val exerciseLog = ExerciseLog(
                        id = 0,
                        index = 0,
                        workoutLogId = workoutLogId,
                        workoutPlanId = workoutPlanSession.workoutPlan.id,
                        exerciseId = exercisePlanSession.exercisePlan.exerciseId,
                        exercisePlanId = exercisePlanSession.exercisePlan.id,
                        note = "",
                        restTimeDuration = 0L,
                    )

                    val exerciseLogId =
                        try {
                            exerciseLogEntityDao.insert(exerciseLog)
                        } catch (e: Exception) {
                            0
                        }

                    val setLogs = exercisePlanSession.setPlans.map { setPlan ->
                        SetLog(
                            id = 0,
                            setTypeIndex = setPlan.index,
                            exerciseLogId = exerciseLogId,
                            exercisePlanId = exercisePlanSession.exercisePlan.id,
                            exerciseId = exercisePlanSession.exercise.id,
                            workoutPlanId = workoutPlanSession.workoutPlan.id,
                            workoutLogId = workoutLogId,
                            weight = PositiveInt(setPlan.weight),
                            reps = PositiveInt(setPlan.reps),
                            prevReps = PositiveInt(0),
                            prevWeight = PositiveInt(0),
                            completed = false,
                            finishTime = 0,
                        )
                    }
                    setLogEntityDao.insert(setLogs)
                }

                workoutLogId
            }
        }
    }

    data class Params(
        val workoutPlanId: Long,
    )
}
