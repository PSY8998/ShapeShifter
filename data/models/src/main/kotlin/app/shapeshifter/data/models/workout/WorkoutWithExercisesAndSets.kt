package app.shapeshifter.data.models.workout

data class WorkoutWithExercisesAndSets(
    val workout: Workout,
    val exercisesWithSets: List<WorkoutExerciseWithSets>,
)
