package app.shapeshifter.data.models.workout

import app.shapeshifter.data.models.Exercise

data class WorkoutExerciseWithSets(
    val id: Long,
    val exercise: Exercise,
    val sets: List<WorkoutExerciseSet>,
)
