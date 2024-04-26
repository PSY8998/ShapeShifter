package app.shapeshifter.feature.exercise.data.exercise

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.shapeshifter.core.base.inject.ApplicationScope
import app.shapeshifter.feature.exercise.data.ShapeShifterDatabase
import app.shapeshifter.feature.exercise.data.exercise.models.Exercise
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

@ApplicationScope
@Inject
class ExerciseDataSource(
    private val shapeShifterDatabase: ShapeShifterDatabase,
) {
    fun insert(
        exercise: Exercise,
    ) {
        shapeShifterDatabase.exerciseQueries.insert(
            id = exercise.id,
            name = exercise.name,
            instructions = exercise.instructions,
        )
    }

    /**
     * listExercises
     **/
    fun allExercises(): Flow<List<Exercise>> {
        return shapeShifterDatabase.exerciseQueries.selectAll(
            mapper = ::Exercise,
        )
            .asFlow()
            .mapToList(Dispatchers.IO)
    }
}
