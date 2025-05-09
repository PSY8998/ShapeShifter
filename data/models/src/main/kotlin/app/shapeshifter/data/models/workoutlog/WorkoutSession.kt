package app.shapeshifter.data.models.workoutlog

import app.shapeshifter.data.models.ExerciseTemplate
import app.shapeshifter.data.models.metrics.WorkoutMetrics
import app.shapeshifter.data.models.workout.ExerciseLog
import app.shapeshifter.data.models.workout.SetLog
import app.shapeshifter.data.models.workout.WorkoutLog
import kotlin.time.Duration
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

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
        val formatPattern = "EEEE, d MMM, yyyy"

        @OptIn(FormatStringsInDatetimeFormats::class)
        val dateTimeFormat = LocalDateTime.Format {
            byUnicodePattern(formatPattern)
        }

        val timeZone = TimeZone.currentSystemDefault()
        return dateTimeFormat.format(workoutLog.startTime.toLocalDateTime(timeZone))
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
