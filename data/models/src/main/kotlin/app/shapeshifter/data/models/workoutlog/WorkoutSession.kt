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

    fun setsOverview(): String {
        var totalSets = 0
        var totalReps = 0
        var totalWeight = 0

        for(set in sets) {
            totalSets += 1
            totalReps += set.reps.value
            totalWeight += set.weight.value
        }

        return "$totalSets sets x $totalReps reps x $totalWeight kg"
    }

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
