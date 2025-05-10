package app.shapeshifter.feature.workout.ui.createworkoutplan

import app.shapeshifter.data.models.ExerciseTemplate
import app.shapeshifter.data.models.workout.ExercisePlan
import app.shapeshifter.data.models.workout.ExercisePlanSession
import app.shapeshifter.data.models.workout.Reps
import app.shapeshifter.data.models.workout.SetPlan
import app.shapeshifter.data.models.workout.Weight
import app.shapeshifter.data.models.workout.WorkoutPlan
import app.shapeshifter.data.models.workout.WorkoutPlanSession
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
            workout = WorkoutPlan(
                id = 0,
                routineId = routineId,
                name = "",
                note = null,
            ),
            exerciseSessions = emptyList(),
        )
    }

    // Load an existing workout plan for editing.
    fun loadExistingPlan(existingPlan: WorkoutPlanSession) {
        val exerciseId: Int =
            (existingPlan.exerciseSessions.minByOrNull { it.exercise.id }?.exercise?.id?.toInt()
                ?: 0) - 1
        val setPlans = existingPlan.exerciseSessions.map { it.sets }.flatten()
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
        val workoutPlan = _currentPlan.value?.workout
            ?: return
        val exerciseWithId = ExercisePlanSession(
            exercise = ExercisePlan(
                id = newExercisePlanTempIdCounter.decrementAndGet().toLong(),
                workoutId = workoutPlan.id,
                exerciseTemplateId = exercise.id,
                index = index,
                note = null,
                restDuration = null,
            ),
            exerciseTemplate = exercise,
            sets = emptyList(),
        )

        _currentPlan.value = _currentPlan.value?.copy(
            exerciseSessions = _currentPlan.value!!.exerciseSessions + exerciseWithId,
        )
    }

    // Update an existing exercise in the current plan.
    fun updateExercise(updatedExercise: ExercisePlanSession) {
        _currentPlan.value = _currentPlan.value?.copy(
            exerciseSessions = _currentPlan.value!!.exerciseSessions.map { exercise ->
                if (exercise.exercise.id == updatedExercise.exercise.id) updatedExercise else exercise
            },
        )
    }

    // Remove an exercise from the current plan.
    fun removeExercise(exerciseId: Long) {
        _currentPlan.value = _currentPlan.value?.copy(
            exerciseSessions = _currentPlan.value!!.exerciseSessions.filter { it.exerciseTemplate.id != exerciseId },
        )
    }

    // Update individual fields of a set plan by its ID.
    fun updateSetPlanValue(
        setPlanId: Long,
        weight: Int? = null,
        reps: Int? = null,
    ) {
        _currentPlan.value?.let { currentPlan ->
            val updatedExercises = currentPlan.exerciseSessions.map { exercise ->
                val updatedSetPlans = exercise.sets.map { setPlan ->
                    if (setPlan.id == setPlanId) {
                        setPlan.copy(
                            weight = weight?.let { Weight(it.toFloat()) } ?: setPlan.weight,
                            reps = reps?.let { Reps(it) } ?: setPlan.reps,
                        )
                    } else {
                        setPlan
                    }
                }
                exercise.copy(sets = updatedSetPlans)
            }
            _currentPlan.value = currentPlan.copy(exerciseSessions = updatedExercises)
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
            val updatedExercises = currentPlan.exerciseSessions.map { exercise ->
                if (exercise.exercise.id == exercisePlanId) {
                    exercise.copy(sets = exercise.sets + setPlanWithId)
                } else {
                    exercise
                }
            }
            _currentPlan.value = currentPlan.copy(exerciseSessions = updatedExercises)
        }
    }
}
