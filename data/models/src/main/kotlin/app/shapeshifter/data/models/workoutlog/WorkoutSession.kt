package app.shapeshifter.data.models.workoutlog

import app.shapeshifter.data.models.Exercise

data class WorkoutSession(
    val workoutLog: WorkoutLog,
    val exerciseSessions: List<ExerciseSession>,
) {
    fun isValid(): Boolean {
        return exerciseSessions.isNotEmpty() && exerciseSessions.all { it.isValid() }
    }
}

data class ExerciseSession(
    val exerciseLog: ExerciseLog,
    val exercise: Exercise,
    val sets: List<SetLog>,
) {
    fun isValid(): Boolean {
        // Check that at least one set is present
        if (sets.isEmpty()) {
            return false
        }

        // Check that all sets are in a complete state and have valid values
        for (set in sets) {
            if (!set.isValid()) {
                return false
            }
        }

        return true
    }
}
