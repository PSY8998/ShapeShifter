package app.shapeshifter.data.db.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.SelectExerciseSession
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.Exercise
import app.shapeshifter.data.models.PositiveInt
import app.shapeshifter.data.models.workoutlog.ExerciseLog
import app.shapeshifter.data.models.workoutlog.ExerciseSession
import app.shapeshifter.data.models.workoutlog.SetLog
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow

interface ExerciseLogEntityDao : EntityDao<ExerciseLog> {
    fun observeWorkoutExercises(workoutId: Long): Flow<List<ExerciseLog>>
    fun exerciseSession(exerciseId: Long): ExerciseSession?
}

@Inject
class SqlDelightExerciseLogEntityDao(
    override val db: ShapeShifterDatabase,
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
) : SqlDelightEntityDao<ExerciseLog>, ExerciseLogEntityDao {
    override fun insert(entity: ExerciseLog): Long {
        db.exercise_logQueries.insert(
            id = entity.id,
            exercise_id = entity.exerciseId,
            workout_id = entity.workoutId,
        )

        return db.exercise_logQueries.lastInsertRowId().executeAsOne()
    }

    override fun update(entity: ExerciseLog) {
        TODO("Not yet implemented")
    }

    override fun deleteEntity(entity: ExerciseLog) {
        TODO("Not yet implemented")
    }

    override fun observeWorkoutExercises(workoutId: Long): Flow<List<ExerciseLog>> {
        return db
            .exercise_logQueries
            .selectAll(
                workout_id = workoutId,
                mapper = { id, exerciseId, wId ->
                    ExerciseLog(
                        id = id,
                        exerciseId = exerciseId,
                        workoutId = wId,
                        note = "",
                    )
                },
            )
            .asFlow()
            .mapToList(appCoroutineDispatchers.io)
    }

    override fun exerciseSession(exerciseId: Long): ExerciseSession? {
        val sessions: List<SelectExerciseSession> = db
            .exercise_sessionQueries.selectExerciseSession(
                exercise_id = exerciseId,
            ).executeAsList()

        if (sessions.isEmpty()) return null

        val firstSession = sessions.first()

        val exerciseLog = ExerciseLog(
            id = firstSession.exercise_log_id,
            workoutId = firstSession.workout_log_id,
            exerciseId = firstSession.exercise_id,
            note = "",
        )

        val exercise = Exercise(
            id = firstSession.exercise_id,
            name = firstSession.exercise_name,
            primaryMuscle = firstSession.exercise_primary_muscle,
            secondaryMuscle = firstSession.exercise_secondary_muscles,
            imageUrl = firstSession.exercise_image_url,
        )

        val setLogs = sessions.map { session ->
            SetLog(
                id = session.set_log_id,
                index = PositiveInt(0),
                prevReps = PositiveInt(session.set_prev_reps.toInt()),
                prevWeight = PositiveInt(session.set_prev_weight.toInt()),
                reps = PositiveInt(session.set_log_reps.toInt()),
                weight = PositiveInt(session.set_log_weight.toInt()),
                completed = true,
                finishTime = session.set_finish_time,
                exerciseLogId = session.exercise_log_id
            )
        }

        return ExerciseSession(
            exerciseLog = exerciseLog,
            exercise = exercise,
            sets = setLogs
        )
    }

}
