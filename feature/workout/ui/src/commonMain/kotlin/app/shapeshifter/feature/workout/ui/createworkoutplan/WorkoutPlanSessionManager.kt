package app.shapeshifter.feature.workout.ui.createworkoutplan

import app.shapeshifter.data.models.ExerciseTemplate
import app.shapeshifter.data.models.plans.ExercisePlanSession
import app.shapeshifter.data.models.plans.WorkoutPlanSession
import app.shapeshifter.data.models.workout.ExercisePlan
import app.shapeshifter.data.models.workout.Reps
import app.shapeshifter.data.models.workout.SetPlan
import app.shapeshifter.data.models.workout.Weight
import app.shapeshifter.data.models.workout.WorkoutPlan
import me.tatarka.inject.annotations.Inject
import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Inject
class WorkoutPlanSessionManager {
    // Expose the current workout plan session as StateFlow for UI reactivity.
    private val _currentPlan = MutableStateFlow<WorkoutPlanSession?>(null)
    val currentPlan: StateFlow<WorkoutPlanSession?> get() = _currentPlan

    // Atomic counters for new entities (using negative numbers until persisted).
    private val newExercisePlanTempIdCounter = AtomicInteger(-1)
    private val newSetPlanTempIdCounter = AtomicInteger(-1)

    // Create a new workout plan session.
    fun createNewPlan(routineId: Long) {
        _currentPlan.value = WorkoutPlanSession(
            workoutPlan = WorkoutPlan(
                id = 0,
                routineId = routineId,
                name = "",
                note = null,
            ),
            exercisePlanSessions = emptyList(),
        )
    }

    // Load an existing workout plan for editing.
    fun loadExistingPlan(existingPlan: WorkoutPlanSession) {
        val exerciseId: Int =
            (existingPlan.exercisePlanSessions.minByOrNull { it.exercisePlan.id }?.exercisePlan?.id?.toInt()
                ?: 0) - 1
        val setPlans = existingPlan.exercisePlanSessions.map { it.setPlans }.flatten()
        val setId =
            (setPlans.minByOrNull { it.id }?.id?.toInt() ?: 0) - 1

        newExercisePlanTempIdCounter.set(exerciseId)
        newSetPlanTempIdCounter.set(setId)
        _currentPlan.value = existingPlan
    }

    fun addExercises(
        exercises: List<ExerciseTemplate>,
        startIndex: Int,
    ) {
        exercises.forEachIndexed { index, exercise ->
            addExercise(
                exercise = exercise,
                index = startIndex + index,
            )
        }
    }

    // Add a new exercise to the current plan.
    private fun addExercise(
        exercise: ExerciseTemplate,
        index: Int,
    ) {
        val workoutPlan = _currentPlan.value?.workoutPlan
            ?: return
        val exerciseWithId = ExercisePlanSession(
            exercisePlan = ExercisePlan(
                id = newExercisePlanTempIdCounter.decrementAndGet().toLong(),
                workoutId = workoutPlan.id,
                exerciseTemplateId = exercise.id,
                index = index,
                note = null,
                restDuration = null,
            ),
            exercise = exercise,
            setPlans = emptyList(),
        )

        _currentPlan.value = _currentPlan.value?.copy(
            exercisePlanSessions = _currentPlan.value!!.exercisePlanSessions + exerciseWithId,
        )
    }

    // Update an existing exercise in the current plan.
    fun updateExercise(updatedExercise: ExercisePlanSession) {
        _currentPlan.value = _currentPlan.value?.copy(
            exercisePlanSessions = _currentPlan.value!!.exercisePlanSessions.map { exercise ->
                if (exercise.exercisePlan.id == updatedExercise.exercisePlan.id) updatedExercise else exercise
            },
        )
    }

    // Remove an exercise from the current plan.
    fun removeExercise(exerciseId: Long) {
        _currentPlan.value = _currentPlan.value?.copy(
            exercisePlanSessions = _currentPlan.value!!.exercisePlanSessions.filter { it.exercisePlan.id != exerciseId },
        )
    }

    // Update individual fields of a set plan by its ID.
    fun updateSetPlanValue(
        setPlanId: Long,
        weight: Int? = null,
        reps: Int? = null,
    ) {
        _currentPlan.value?.let { currentPlan ->
            val updatedExercises = currentPlan.exercisePlanSessions.map { exercise ->
                val updatedSetPlans = exercise.setPlans.map { setPlan ->
                    if (setPlan.id == setPlanId) {
                        setPlan.copy(
                            weight = weight?.let { Weight(it.toFloat()) } ?: setPlan.weight,
                            reps = reps?.let { Reps(it) } ?: setPlan.reps,
                        )
                    } else {
                        setPlan
                    }
                }
                exercise.copy(setPlans = updatedSetPlans)
            }
            _currentPlan.value = currentPlan.copy(exercisePlanSessions = updatedExercises)
        }
    }

    // Add a new set plan to an exercise.
    // If the provided set plan has an ID of 0, assign a temporary negative ID.
    fun addSetPlanToExercise(
        exercisePlanId: Long, newSetPlan: SetPlan,
    ) {
        val setPlanWithId = if (newSetPlan.id == 0L) {
            newSetPlan.copy(id = newSetPlanTempIdCounter.decrementAndGet().toLong())
        } else {
            newSetPlan
        }
        _currentPlan.value?.let { currentPlan ->
            val updatedExercises = currentPlan.exercisePlanSessions.map { exercise ->
                if (exercise.exercisePlan.id == exercisePlanId) {
                    exercise.copy(setPlans = exercise.setPlans + setPlanWithId)
                } else {
                    exercise
                }
            }
            _currentPlan.value = currentPlan.copy(exercisePlanSessions = updatedExercises)
        }
    }
}
