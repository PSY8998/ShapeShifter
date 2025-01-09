package app.shapeshifter.feature.workout.domain

import app.shapeshifter.data.db.daos.SqlDelightExercisePlanEntityDao
import app.shapeshifter.data.db.daos.SqlDelightSetPlanEntityDao
import app.shapeshifter.data.db.daos.SqlDelightWorkoutPlanEntityDao
import app.shapeshifter.data.models.plans.ExercisePlanSession
import app.shapeshifter.data.models.plans.WorkoutPlanSession
import app.shapeshifter.domain.FlowUseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Inject
class FetchWorkoutPlanSessionsUseCase(
    private val workoutPlanEntityDao: SqlDelightWorkoutPlanEntityDao,
    private val exercisePlanEntityDao: SqlDelightExercisePlanEntityDao,
    private val setPlanEntityDao: SqlDelightSetPlanEntityDao,
) : FlowUseCase<Unit, List<WorkoutPlanSession>>() {
    override fun createObservable(params: Unit): Flow<List<WorkoutPlanSession>> {
        val workoutPlans = workoutPlanEntityDao.observeWorkoutPlans()
        val exercisePlans = exercisePlanEntityDao.observeExercisePlans()

        val setPlans = setPlanEntityDao.observeSetPlans()

        return combine(
            workoutPlans,
            exercisePlans,
            setPlans,
        ) { workouts, exercises, sets ->
            val workoutSessions = workouts.map { workout ->
                val workoutExercises = exercises.filter { it.first.workoutPlanId == workout.id }
                val exercisePlanSessions = workoutExercises.map { workoutExercise ->
                    val exerciseSetPlans = sets.filter { it.exercisePlanId == workoutExercise.first.id }
                    ExercisePlanSession(
                        exercisePlan = workoutExercise.first,
                        exercise = workoutExercise.second,
                        setPlans = exerciseSetPlans
                    )
                }
                WorkoutPlanSession(
                    workoutPlan = workout,
                    exercisePlanSessions = exercisePlanSessions,
                )
            }
            workoutSessions
        }
    }

}
