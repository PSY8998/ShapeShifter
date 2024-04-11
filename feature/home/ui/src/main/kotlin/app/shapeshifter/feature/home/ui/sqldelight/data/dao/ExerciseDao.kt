package app.shapeshifter.feature.home.ui.sqldelight.data.dao

import app.shapeshifter.feature.home.ui.Database
import app.shapeshifter.feature.home.ui.sqldelight.data.Exercise
import me.tatarka.inject.annotations.Inject

@Inject
class ExerciseDao(
    private val db: Database,
) {
    fun testDb() {
        val hdj = db.exerciseQueries.selectAll { id, name, instructions ->
            Exercise(id, name, instructions)
        }.executeAsList()
    }
}
