package app.shapeshifter.feature.workout.domain

import app.shapeshifter.data.db.daos.ExerciseEntityDao
import app.shapeshifter.data.models.Exercise
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject

@Inject
class FetchExercisesUseCase(
    val exerciseEntityDao: ExerciseEntityDao
) : UseCase<List<Long>, List<Exercise>>(){
    override suspend fun doWork(params: List<Long>): List<Exercise> {
        return exerciseEntityDao.select(params)
    }
}
