package app.shapeshifter.feature.workout.domain

import app.shapeshifter.data.models.plans.WorkoutPlanSession
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject

@Inject
class SaveWorkoutUseCase() : UseCase<WorkoutPlanSession, Unit>() {
    override suspend fun doWork(params: WorkoutPlanSession) {
        val workoutPlan = params.workoutPlan

        // save plan

        for (exercisePlanSession in params.exercisePlanSessions) {
            val exercisePlan = exercisePlanSession.exercisePlan
            // save exercisePlan

            val sets = exercisePlanSession.setPlans
            // save setPlans
        }
    }

}
