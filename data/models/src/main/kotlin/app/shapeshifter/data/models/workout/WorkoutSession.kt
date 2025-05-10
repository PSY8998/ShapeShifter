package app.shapeshifter.data.models.workout

import app.shapeshifter.data.models.ExerciseTemplate
import app.shapeshifter.data.models.metrics.WorkoutMetrics
import kotlin.time.Duration
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

interface WorkoutSession {
    val workout: Workout
    val exerciseSessions: List<ExerciseSession>
}

interface ExerciseSession {
    val exercise: Exercise
    val exerciseTemplate: ExerciseTemplate
    val sets: List<Set>
}

data class WorkoutPlanSession(
    override val workout: WorkoutPlan,
    override val exerciseSessions: List<ExercisePlanSession>,
) : WorkoutSession

data class WorkoutLogSession(
    override val workout: WorkoutLog,
    override val exerciseSessions: List<ExerciseLogSession>,
) : WorkoutSession {
    fun isValid(): Boolean {
        return exerciseSessions.isNotEmpty() && exerciseSessions.all { it.isValid() }
    }

    fun metrics(): WorkoutMetrics {
        val duration = workout.finishTime?.let {
            it - workout.startTime
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
        return dateTimeFormat.format(workout.startTime.toLocalDateTime(timeZone))
    }
}

data class ExercisePlanSession(
    override val exercise: ExercisePlan,
    override val exerciseTemplate: ExerciseTemplate,
    override val sets: List<SetPlan>,
) : ExerciseSession

data class ExerciseLogSession(
    override val exercise: ExerciseLog,
    override val exerciseTemplate: ExerciseTemplate,
    override val sets: List<SetLog>,
) : ExerciseSession {
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
