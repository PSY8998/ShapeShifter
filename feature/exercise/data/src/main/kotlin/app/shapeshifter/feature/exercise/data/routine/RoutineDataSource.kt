package app.shapeshifter.feature.exercise.data.routine

import app.shapeshifter.feature.exercise.data.ShapeShifterDatabase
import app.shapeshifter.feature.exercise.data.routine.models.Routine
import me.tatarka.inject.annotations.Inject

@Inject
class RoutineDataSource(
    private val shapeShifterDatabase: ShapeShifterDatabase
) {
    fun insert(
        routine: Routine
    ){
        shapeShifterDatabase.routineQueries.insert(
            id = routine.id,
            name = routine.name
        )
    }
}
