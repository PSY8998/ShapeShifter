package app.shapeshifter.feature.workout.domain

import app.shapeshifter.data.models.workout.WorkoutWithSaveInfo
import app.shapeshifter.domain.FlowUseCase
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveActiveWorkoutUseCase : FlowUseCase<Unit, WorkoutWithSaveInfo> {

}
