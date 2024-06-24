package app.shapeshifter.data.models.plans

import app.shapeshifter.data.models.Exercise

data class WorkoutPlanSession(
    val workoutPlan: WorkoutPlan,
    val exercisePlanSessions: List<ExercisePlanSession>
)

data class ExercisePlanSession(
    val exercisePlan: ExercisePlan,
    val exercise: Exercise,
    val setPlans: List<SetPlan>
)
