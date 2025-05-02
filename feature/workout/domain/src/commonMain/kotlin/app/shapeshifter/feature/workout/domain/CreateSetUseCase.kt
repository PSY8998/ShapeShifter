package app.shapeshifter.feature.workout.domain

import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.daos.SetLogEntityDao
import app.shapeshifter.data.db.daos.SqlDelightSetLogEntityDao
import app.shapeshifter.data.models.PositiveInt
import app.shapeshifter.data.models.workoutlog.SetLog
import app.shapeshifter.domain.UseCase
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.withContext

@Inject
class CreateSetUseCase(
    private val dispatchers: AppCoroutineDispatchers,
    private val dao: SetLogEntityDao,
) : UseCase<CreateSetUseCase.Params, Long>() {
    override suspend fun doWork(params: Params): Long {
        return withContext(dispatchers.databaseWrite) {
            // Get the last set to determine the set type and index
            val lastSet = (dao as SqlDelightSetLogEntityDao).db.set_logQueries.exerciseLastSet(
                exerciseLogId = params.exerciseLogId,
            ).executeAsOneOrNull()

            // Use the set type from the previous set, or default to 2 (Normal)
            val setTypeId = lastSet?.set_type_id ?: 2L

            // Debug info for troubleshooting
            println("Creating new set with type ID: $setTypeId (from last set: $lastSet)")

            val setLog = SetLog(
                id = 0L,
                exerciseLogId = params.exerciseLogId,
                setIndex = PositiveInt(1), // The DAO will calculate the correct index
                weight = PositiveInt(0),
                reps = PositiveInt(0),
                prevWeight = PositiveInt(0),
                prevReps = PositiveInt(0),
                completed = false,
                finishTime = 0,
                exercisePlanId = null,
                exerciseId = params.exerciseId,
                workoutPlanId = params.workoutPlanId,
                workoutLogId = params.workoutLogId,
                setTypeId = setTypeId,
            )

            dao.insert(setLog)
        }
    }

    data class Params(
        val exerciseLogId: Long,
        val exerciseId: Long,
        val workoutPlanId: Long,
        val workoutLogId: Long,
    )

}
