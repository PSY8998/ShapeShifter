package app.shapeshifter.feature.workout.domain

import app.shapeshifter.data.db.daos.ExerciseEntityDao
import app.shapeshifter.data.models.ExerciseTemplate
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject

@Inject
class FetchExercisesUseCase(
    val exerciseEntityDao: ExerciseEntityDao,
) : UseCase<List<Long>, List<ExerciseTemplate>>() {
    override suspend fun doWork(params: List<Long>): List<ExerciseTemplate> {
        return exerciseEntityDao.select(params)
    }
}
