package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.models.PositiveInt
import app.shapeshifter.data.models.plans.WorkoutPlanSession
import app.shapeshifter.data.models.workoutlog.ExerciseLog
import app.shapeshifter.data.models.workoutlog.ExerciseSession
import app.shapeshifter.data.models.workoutlog.SetLog
import app.shapeshifter.data.models.workoutlog.WorkoutLog
import app.shapeshifter.data.models.workoutlog.WorkoutSession
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject

@Inject
class GetWorkoutSessionUseCase(
    private val selectWorkoutPlanUseCase: SelectWorkoutPlanUseCase,
    private val dispatchers: AppCoroutineDispatchers,
) : UseCase<GetWorkoutSessionUseCase.Params, WorkoutSession>() {

    override suspend fun doWork(params: Params): WorkoutSession {
        return if (params.workoutPlanId == -1L) {
            WorkoutSession(
                workoutLog = WorkoutLog.emptyQuickWorkout(),
                exerciseSessions = emptyList(),
            )
        } else {
            val workoutPlanSession = selectWorkoutPlanUseCase(
                SelectWorkoutPlanUseCase.Params(params.workoutPlanId),
            ).getOrNull()
            convertToWorkoutSession(workoutPlanSession)
        }


    }

    private fun convertToWorkoutSession(workoutPlanSession: WorkoutPlanSession?): WorkoutSession {
        val exerciseSessions =
            workoutPlanSession!!.exercisePlanSessions.map { exercisePlanSession ->
                ExerciseSession(
                    exerciseLog = ExerciseLog(
                        id = exercisePlanSession.exercisePlan.id,
                        index = 0,
                        workoutLogId = workoutPlanSession.workoutPlan.id,
                        workoutPlanId = workoutPlanSession.workoutPlan.id,
                        exerciseId = exercisePlanSession.exercisePlan.exerciseId,
                        exercisePlanId = exercisePlanSession.exercisePlan.id,
                        note = "",
                        restTimeDuration = 0L,
                    ),
                    exercise = exercisePlanSession.exercise,
                    sets = exercisePlanSession.setPlans.map { setPlan ->
                        SetLog(
                            id = setPlan.id,
                            setIndex = setPlan.index,
                            exerciseLogId = exercisePlanSession.exercisePlan.id,
                            exercisePlanId = exercisePlanSession.exercisePlan.id,
                            exerciseId = exercisePlanSession.exercise.id,
                            workoutPlanId = workoutPlanSession.workoutPlan.id,
                            workoutLogId = workoutPlanSession.workoutPlan.id,
                            weight = PositiveInt(setPlan.weight),
                            reps = PositiveInt(setPlan.reps),
                            prevReps = PositiveInt(0),
                            prevWeight = PositiveInt(0),
                            completed = false,
                            finishTime = 0,
                        )
                    }
                )
            }
        return WorkoutSession(
            workoutLog = WorkoutLog(
                id = workoutPlanSession.workoutPlan.id,
                workoutPlanId = workoutPlanSession.workoutPlan.id,
                name = workoutPlanSession.workoutPlan.name,
                startTimeInMillis = System.currentTimeMillis(),
                finishTimeInMillis = 0L,
                note = "",
                restFinishTimeInMillis = 0L
            ),
            exerciseSessions = exerciseSessions
        )
    }


    data class Params(
        val workoutPlanId: Long,
    )
}
