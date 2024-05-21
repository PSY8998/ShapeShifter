package app.shapeshifter.data.models.workoutlog

import app.shapeshifter.data.models.Exercise

data class WorkoutSession(
    val workout: WorkoutLog,
    val exercises: List<ExerciseSession>,
) {
    fun isValid(): Boolean {
        return exercises.all { it.isValid() }
    }
}

data class ExerciseSession(
    val exerciseLog: ExerciseLog,
    val exercise: Exercise,
    val sets: List<SetLog>,
) {
    fun isValid(): Boolean {
        if (sets.isEmpty()) return false
        return sets.all { it.isValid() }
    }
}
