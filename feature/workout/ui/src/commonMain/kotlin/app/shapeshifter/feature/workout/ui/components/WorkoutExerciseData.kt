package app.shapeshifter.feature.workout.ui.components

data class WorkoutExercise(
    val note: String,
    val name: String,
    val sets: List<WorkoutExerciseSet>,
)

data class WorkoutExerciseSet(
    val index: Int,
    val weight: Int,
    val reps: Int,
    val completed: Boolean,
    val previousSet: WorkoutExercisePreviousSet
)

data class WorkoutExercisePreviousSet(
    val weight: Int,
    val reps: Int,
)

val sampleWorkoutExercise = WorkoutExercise(
    note = "",
    name = "Deadlift",
    sets = listOf(
        WorkoutExerciseSet(
            index = 1,
            weight = 40,
            reps = 4,
            completed = false,
            previousSet = WorkoutExercisePreviousSet(
                weight = 30,
                reps = 4
            )
        )
    )
)
