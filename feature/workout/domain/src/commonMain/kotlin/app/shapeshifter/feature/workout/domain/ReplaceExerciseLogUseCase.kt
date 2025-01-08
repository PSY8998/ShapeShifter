package app.shapeshifter.feature.workout.domain

import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject

@Inject
class ReplaceExerciseLogUseCase(
) : UseCase<ReplaceExerciseLogUseCase.Params, Unit>(){

    // get existing exerciseLog
    // get index of that log
    // remove the log
    // create new exerciselog with stored index
    override suspend fun doWork(params: Params) {

    }

    data class Params(
        val exerciseLogId: Long,
        val exerciseId: Long,
    )


}
