package app.shapeshifter.feature.exercise.data.exercise

import app.shapeshifter.core.base.inject.ApplicationScope
import app.shapeshifter.data.db.daos.ExerciseEntityDao
import app.shapeshifter.data.models.Exercise
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow

@ApplicationScope
@Inject
class ExerciseRepository(
    private val exerciseEntityDao: ExerciseEntityDao,
) {
    fun insert(exercise: Exercise) {
        exerciseEntityDao.insert(exercise)
    }

    fun observeExercises(): Flow<List<Exercise>> {
        return exerciseEntityDao.observeExercises()
    }
}
