package app.shapeshifter.feature.exercise.data

import app.shapeshifter.feature.exercise.data.models.Exercise
import me.tatarka.inject.annotations.Inject

@Inject
class ExerciseRepository(
    private val exerciseDataSource: ExerciseDataSource
) {
    fun insert(exercise: Exercise){
        exerciseDataSource.insert(exercise)
    }
}
