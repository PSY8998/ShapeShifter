package app.shapeshifter.feature.exercise.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.domain.UseCase
import app.shapeshifter.feature.exercise.data.exercise.ExerciseRepository
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.withContext

@Inject
class FetchExercisesUseCase(
    private val exerciseRepository: ExerciseRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : UseCase<Unit, Unit>() {
    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            exerciseRepository.fetchExercises()
        }
    }
}
