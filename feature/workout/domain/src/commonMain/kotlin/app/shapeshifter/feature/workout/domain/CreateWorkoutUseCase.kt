package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.daos.WorkoutEntityDao
import app.shapeshifter.data.models.workoutlog.WorkoutLog
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

@Inject
class CreateWorkoutUseCase(
    private val workoutEntityDao: WorkoutEntityDao,
    private val dispatchers: AppCoroutineDispatchers,
) : UseCase<Unit, Long>() {
    override suspend fun doWork(params: Unit): Long {
        return withContext(dispatchers.databaseRead) {
            val workout = workoutEntityDao.activeWorkout().firstOrNull()

            // TODO: the check should throw error as the use case should only create workout
            if (workout != null) {
                return@withContext workout.workout.id
            }

            withContext(dispatchers.databaseWrite) {
                workoutEntityDao.insert(WorkoutLog.emptyQuickWorkout())
            }
        }
    }
}
