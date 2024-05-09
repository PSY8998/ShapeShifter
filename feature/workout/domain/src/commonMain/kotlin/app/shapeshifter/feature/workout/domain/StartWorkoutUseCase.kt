package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.daos.WorkoutEntityDao
import app.shapeshifter.data.models.workoutlog.WorkoutLog
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

@Inject
class StartWorkoutUseCase(
    private val workoutEntityDao: WorkoutEntityDao,
    private val dispatchers: AppCoroutineDispatchers,
) : UseCase<Unit, StartWorkoutUseCase.StartWorkoutResult>() {
    override suspend fun doWork(params: Unit): StartWorkoutResult {
        val activeWorkout = withContext(dispatchers.io) {
            workoutEntityDao.activeWorkout().firstOrNull()
        }

        if (activeWorkout != null) {
            return StartWorkoutResult.WorkoutInProgress(
                workoutId = activeWorkout.workout.id,
                workoutName = "Empty Workout",
            )
        }

        // do not insert
        val workoutLogId = withContext(dispatchers.databaseWrite) {
            workoutEntityDao.insert(WorkoutLog.emptyQuickWorkout())
        }

        return StartWorkoutResult.Workout(workoutLogId)
    }

    sealed interface StartWorkoutResult {
        data class Workout(
            val workoutId: Long,
        ) : StartWorkoutResult

        data class WorkoutInProgress(
            val workoutId: Long,
            val workoutName: String,
        ) : StartWorkoutResult
    }
}
