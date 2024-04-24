package app.shapeshifter.feature.exercise.data

import app.shapeshifter.feature.exercise.data.models.Exercise
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow

@Inject
class ExerciseRepository(
    private val exerciseDataSource: ExerciseDataSource
) {
    fun insert(exercise: Exercise){
        exerciseDataSource.insert(exercise)
    }

    fun allExercises(): Flow<List<Exercise>> {
        return exerciseDataSource.allExercises()
    }
}
