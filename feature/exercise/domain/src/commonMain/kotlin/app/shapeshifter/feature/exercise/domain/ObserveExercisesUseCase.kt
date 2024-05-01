package app.shapeshifter.feature.exercise.domain

import app.shapeshifter.data.models.Exercise
import app.shapeshifter.domain.FlowUseCase
import app.shapeshifter.feature.exercise.data.exercise.ExerciseRepository
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow

@Inject
class ObserveExercisesUseCase(
    private val exerciseRepository: ExerciseRepository,
) : FlowUseCase<Unit, List<Exercise>>() {
    override fun createObservable(params: Unit): Flow<List<Exercise>> {
        return exerciseRepository.observeExercises()
    }
}
