package app.shapeshifter.feature.exercise.data.exercise

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.core.base.inject.ApplicationScope
import app.shapeshifter.data.db.daos.ExerciseEntityDao
import app.shapeshifter.data.models.Exercise
import app.shapeshifter.data.supabase.datasources.ExerciseDataSource
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@ApplicationScope
@Inject
class ExerciseRepository(
    private val exerciseEntityDao: ExerciseEntityDao,
    private val exerciseDataSource: ExerciseDataSource,
    private val dispatchers: AppCoroutineDispatchers,
) {
    suspend fun fetchExercises() {
        val exercises = exerciseDataSource.exercises()
        withContext(dispatchers.databaseWrite) {
            exerciseEntityDao.insert(exercises)
        }
    }

    fun observeExercises(): Flow<List<Exercise>> {
        return exerciseEntityDao.observeExercises()
    }
}
