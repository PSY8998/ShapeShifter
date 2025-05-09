package app.shapeshifter.data.models.workoutlog

import app.shapeshifter.data.models.ExerciseTemplate
import app.shapeshifter.data.models.metrics.WorkoutMetrics
import app.shapeshifter.data.models.workout.ExerciseLog
import app.shapeshifter.data.models.workout.SetLog
import app.shapeshifter.data.models.workout.WorkoutLog
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Duration

data class WorkoutSession(
    val workoutLog: WorkoutLog,
    val exerciseSessions: List<ExerciseSession>,
) {
    fun isValid(): Boolean {
        return exerciseSessions.isNotEmpty() && exerciseSessions.all { it.isValid() }
    }

    fun metrics(): WorkoutMetrics {
        val duration = workoutLog.finishTime?.let {
            it - workoutLog.startTime
        } ?: Duration.ZERO

        val calories = 200
        return WorkoutMetrics(
            duration = duration,
            calories = calories,
        )
    }

    fun formatStartTime(): String {
        val formatter = DateTimeFormatter.ofPattern("EEEE, d MMM, yyyy")
        val dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(workoutLog.startTime.inWholeMilliseconds),
            ZoneId.systemDefault(),
        )
        return dateTime.format(formatter)
    }


}

data class ExerciseSession(
    val exerciseLog: ExerciseLog,
    val exercise: ExerciseTemplate,
    val sets: List<SetLog>,
) {

    fun setsOverview(): String {
        var totalSets = 0
        var totalReps = 0
        var totalWeight = 0f

        for (set in sets) {
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
