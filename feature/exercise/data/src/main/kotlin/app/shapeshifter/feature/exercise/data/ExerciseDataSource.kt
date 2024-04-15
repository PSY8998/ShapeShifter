package app.shapeshifter.feature.exercise.data

import app.shapeshifter.feature.exercise.data.models.Exercise
import me.tatarka.inject.annotations.Inject

@Inject
class ExerciseDataSource(
    private val shapeShifterDatabase: ShapeShifterDatabase,
) {
    fun insert(
        exercise: Exercise
    ){
        shapeShifterDatabase.exerciseQueries.insert(
            id = exercise.id,
            name = exercise.name,
            instructions = exercise.instructions
        )
    }
}
