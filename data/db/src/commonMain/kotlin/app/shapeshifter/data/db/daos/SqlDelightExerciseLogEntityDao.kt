package app.shapeshifter.data.db.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.workout.ExerciseLog
import me.tatarka.inject.annotations.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.flow.Flow

interface ExerciseLogEntityDao {
    fun insert(entity: ExerciseLog): Long
    fun update(entity: ExerciseLog)
    fun observeWorkoutExercises(workoutId: Long): Flow<List<ExerciseLog>>
    fun delete(exerciseLogId: Long)
}

@Inject
class SqlDelightExerciseLogEntityDao(
    val db: ShapeShifterDatabase,
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
) : ExerciseLogEntityDao {

    override fun insert(entity: ExerciseLog): Long {
        db.exercise_logQueries.insert(
            id = entity.id,
            exerciseIndex = entity.index.toLong(),
            workoutLogId = entity.workoutId,
            exerciseTemplateId = entity.exerciseTemplateId,
            exercisePlanId = entity.exercisePlanId,
            restTimeDuration = entity.restDuration?.inWholeMilliseconds ?: 0,
        )

        return db.exercise_logQueries.lastInsertRowId().executeAsOne()
    }

    override fun update(entity: ExerciseLog) {
        db.exercise_logQueries.update(
            restTimeDuration = entity.restDuration?.inWholeMilliseconds ?: 0,
            id = entity.id,
            exerciseIndex = entity.index.toLong(),
        )
    }

    override fun delete(exerciseLogId: Long) {
        db.exercise_logQueries.delete(exerciseLogId)
    }

    override fun observeWorkoutExercises(workoutId: Long): Flow<List<ExerciseLog>> {
        return db
            .exercise_logQueries
            .selectAll(
                workout_id = workoutId,
                mapper = { id, index, exerciseTemplateId, workoutLogId, exercisePlanId, restTimeDuration ->
                    ExerciseLog(
                        id = id,
                        index = index.toInt(),
                        exerciseTemplateId = exerciseTemplateId,
                        workoutId = workoutLogId,
                        exercisePlanId = exercisePlanId,
                        note = "",
                        restDuration = restTimeDuration.toDuration(DurationUnit.MILLISECONDS),
                    )
                },
            )
            .asFlow()
            .mapToList(appCoroutineDispatchers.io)
    }
}
