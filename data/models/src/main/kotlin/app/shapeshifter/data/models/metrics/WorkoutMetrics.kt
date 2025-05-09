package app.shapeshifter.data.models.metrics

import kotlin.time.Duration

data class WorkoutMetrics(
    val duration: Duration,
    val calories: Int,
)
