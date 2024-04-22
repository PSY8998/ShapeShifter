package app.shapeshifter.feature.exercise.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.shapeshifter.core.base.inject.ApplicationScope
import app.shapeshifter.feature.exercise.data.models.Exercise
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    /**
     * listExercises
     **/
    fun allExercises(): Flow<List<Exercise>> {
        return shapeShifterDatabase.exerciseQueries.selectAll(
            mapper = ::Exercise
        )
            .asFlow()
            .mapToList(Dispatchers.IO)
    }
}
