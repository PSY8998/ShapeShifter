package app.shapeshifter.data.models.workoutlog

import app.shapeshifter.data.models.Exercise

data class WorkoutSession(
    val workout: WorkoutLog,
    val exercises: List<ExerciseSession>,
)

data class ExerciseSession(
    val exerciseLog: ExerciseLog,
    val exercise: Exercise,
    val sets: List<SetLog>,
)
