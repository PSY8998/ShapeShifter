package app.shapeshifter.data.models.plans

import app.shapeshifter.data.models.ExerciseTemplate
import app.shapeshifter.data.models.workout.ExercisePlan
import app.shapeshifter.data.models.workout.SetPlan
import app.shapeshifter.data.models.workout.WorkoutPlan

data class WorkoutPlanSession(
    val workoutPlan: WorkoutPlan,
    val exercisePlanSessions: List<ExercisePlanSession>
)

data class ExercisePlanSession(
    val exercisePlan: ExercisePlan,
    val exercise: ExerciseTemplate,
    val setPlans: List<SetPlan>
)
