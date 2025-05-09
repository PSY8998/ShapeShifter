package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.daos.WorkoutEntityDao
import app.shapeshifter.data.models.workoutlog.WorkoutSession
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

@Inject
class FinishWorkoutUseCase(
    private val dispatchers: AppCoroutineDispatchers,
    private val dao: WorkoutEntityDao,
) : UseCase<FinishWorkoutUseCase.Params, Long>() {
    override suspend fun doWork(params: Params): Long {
        withContext(dispatchers.computation) {
            val isValid = params.workoutSession.isValid()
            if (isValid.not()) {
                throw IllegalStateException("Not a valid workout")
            }
        }

        return withContext(dispatchers.databaseWrite) {
            // Calculate workout duration based on start time and current time
            val workoutLog = params.workoutSession.workoutLog
            val startTime = workoutLog.startTime
            val currentTime = Clock.System.now()
            val duration = currentTime - startTime

            // Use the same duration but apply it to the custom date if provided
            val effectiveStartTime = startTime
            val effectiveFinishTime = effectiveStartTime + duration

            dao.upsert(
                entity = workoutLog.copy(
                    finishTime = effectiveFinishTime,
                ),
            )
        }
    }

    data class Params(
        val workoutSession: WorkoutSession,
    )
}
