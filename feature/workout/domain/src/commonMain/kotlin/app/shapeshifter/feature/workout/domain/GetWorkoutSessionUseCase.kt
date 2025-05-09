package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.models.plans.WorkoutPlanSession
import app.shapeshifter.data.models.workout.ExerciseLog
import app.shapeshifter.data.models.workout.Reps
import app.shapeshifter.data.models.workout.SetLog
import app.shapeshifter.data.models.workout.Weight
import app.shapeshifter.data.models.workout.WorkoutLog
import app.shapeshifter.data.models.workoutlog.ExerciseSession
import app.shapeshifter.data.models.workoutlog.WorkoutSession
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration
import kotlinx.datetime.Clock

@Inject
class GetWorkoutSessionUseCase(
    private val selectWorkoutPlanUseCase: SelectWorkoutPlanUseCase,
    private val dispatchers: AppCoroutineDispatchers,
) : UseCase<GetWorkoutSessionUseCase.Params, WorkoutSession>() {

    override suspend fun doWork(params: Params): WorkoutSession {
        return if (params.workoutPlanId == null) {
            WorkoutSession(
                workoutLog = WorkoutLog.empty(
                    name = "Quick Workout",
                    startTime = Clock.System.now(),
                ),
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
                        exercisePlanId = exercisePlanSession.exercisePlan.id,
                        note = "",
                        workoutId = workoutPlanSession.workoutPlan.id,
                        exerciseTemplateId = exercisePlanSession.exercisePlan.exerciseTemplateId,
                        restDuration = Duration.ZERO,
                    ),
                    exercise = exercisePlanSession.exercise,
                    sets = exercisePlanSession.setPlans.map { setPlan ->
                        SetLog(
                            id = setPlan.id,
                            exerciseId = exercisePlanSession.exercise.id,
                            weight = setPlan.weight,
                            reps = setPlan.reps,
                            setTypeId = 2,
                            index = setPlan.index,
                            setPlanId = null,
                            isCompleted = false,
                            previousWeight = Weight.ZERO,
                            previousReps = Reps.ZERO,
                        )
                    },
                )
            }
        return WorkoutSession(
            workoutLog = WorkoutLog(
                id = workoutPlanSession.workoutPlan.id,
                workoutPlanId = workoutPlanSession.workoutPlan.id,
                name = workoutPlanSession.workoutPlan.name,
                note = "",
                startTime = Clock.System.now(),
                finishTime = null,
            ),
            exerciseSessions = exerciseSessions,
        )
    }


    data class Params(
        val workoutPlanId: Long?,
    )
}
