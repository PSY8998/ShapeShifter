package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.models.workout.ExerciseLog
import app.shapeshifter.data.models.workout.ExerciseLogSession
import app.shapeshifter.data.models.workout.Reps
import app.shapeshifter.data.models.workout.SetLog
import app.shapeshifter.data.models.workout.Weight
import app.shapeshifter.data.models.workout.WorkoutLog
import app.shapeshifter.data.models.workout.WorkoutLogSession
import app.shapeshifter.data.models.workout.WorkoutPlanSession
import app.shapeshifter.data.models.workout.WorkoutSession
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlin.time.Clock
import kotlin.time.Duration

@Inject
class GetWorkoutSessionUseCase(
    private val selectWorkoutPlanUseCase: SelectWorkoutPlanUseCase,
    private val dispatchers: AppCoroutineDispatchers,
) : UseCase<GetWorkoutSessionUseCase.Params, WorkoutSession>() {

    override suspend fun doWork(params: Params): WorkoutSession {
        return if (params.workoutPlanId == null) {
            WorkoutLogSession(
                workout = WorkoutLog.empty(
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
            workoutPlanSession!!.exerciseSessions.map { exercisePlanSession ->
                ExerciseLogSession(
                    exercise = ExerciseLog(
                        id = exercisePlanSession.exercise.id,
                        index = 0,
                        exercisePlanId = exercisePlanSession.exercise.id,
                        note = "",
                        workoutId = workoutPlanSession.workout.id,
                        exerciseTemplateId = exercisePlanSession.exercise.exerciseTemplateId,
                        restDuration = Duration.ZERO,
                    ),
                    exerciseTemplate = exercisePlanSession.exerciseTemplate,
                    sets = exercisePlanSession.sets.map { setPlan ->
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
        return WorkoutLogSession(
            workout = WorkoutLog(
                id = workoutPlanSession.workout.id,
                workoutPlanId = workoutPlanSession.workout.id,
                name = workoutPlanSession.workout.name,
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
