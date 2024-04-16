package app.shapeshifter.feature.exercise.data

import app.shapeshifter.core.base.inject.ApplicationScope
import app.shapeshifter.feature.exercise.data.models.Exercise
import me.tatarka.inject.annotations.Inject

@ApplicationScope
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
