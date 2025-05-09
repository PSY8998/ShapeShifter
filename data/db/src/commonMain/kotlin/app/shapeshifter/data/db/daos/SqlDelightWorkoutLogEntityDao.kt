package app.shapeshifter.data.db.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.ExerciseTemplate
import app.shapeshifter.data.models.workout.ExerciseLog
import app.shapeshifter.data.models.workout.Reps
import app.shapeshifter.data.models.workout.SetLog
import app.shapeshifter.data.models.workout.Weight
import app.shapeshifter.data.models.workout.WorkoutLog
import app.shapeshifter.data.models.workoutlog.ExerciseSession
import app.shapeshifter.data.models.workoutlog.WorkoutSession
import app.shapeshifter.data.models.workoutlog.WorkoutSessionOverview
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.datetime.Instant

interface WorkoutEntityDao {
    fun insert(entity: WorkoutLog): Long

    fun upsert(entity: WorkoutLog): Long

    fun update(entity: WorkoutLog)

    fun deleteEntity(entity: WorkoutLog)

    fun observeWorkoutWithExercisesAndSets(
        workoutLogId: Long,
    ): Flow<WorkoutSession>

    fun observeWorkoutSessions(): Flow<List<WorkoutSession>>

    fun activeWorkout(): Flow<WorkoutSessionOverview?>

}

@Inject
class SqlDelightWorkoutEntityDao(
    val db: ShapeShifterDatabase,
    private val transactionRunner: DatabaseTransactionRunner,
    private val dispatchers: AppCoroutineDispatchers,
) : WorkoutEntityDao {
    override fun insert(entity: WorkoutLog): Long {
        return transactionRunner {
            db.workout_logQueries.insert(
                id = entity.id,
                workoutPlanId = entity.workoutPlanId,
                name = entity.name,
                startTime = entity.startTime.toEpochMilliseconds(),
                finishTime = entity.finishTime?.toEpochMilliseconds(),
                restFinishAt = 0,
            )
            db.workout_logQueries.lastInsertRowId().executeAsOne()
        }
    }

    override fun upsert(entity: WorkoutLog): Long {
        return if (entity.id != 0L) {
            update(entity)
            entity.id
        } else {
            insert(entity)
        }
    }

    override fun update(entity: WorkoutLog) {
        db.workout_logQueries.update(
            workoutPlanId = entity.workoutPlanId,
            name = entity.name,
            startTime = entity.startTime.toEpochMilliseconds(),
            finishTime = entity.finishTime?.toEpochMilliseconds(),
            id = entity.id,
            restFinishAt = 0,
        )
    }

    override fun deleteEntity(entity: WorkoutLog) {
        db.workout_logQueries.delete(entity.id)
    }

    override fun observeWorkoutWithExercisesAndSets(
        workoutLogId: Long,
    ): Flow<WorkoutSession> {
        return db.workout_sessionQueries
            .selectWorkoutSession(workoutLogId = workoutLogId)
            .asFlow()
            .mapToList(dispatchers.io)
            .mapNotNull { items ->
                // Return null if no items are found
                val firstItem = items.firstOrNull() ?: return@mapNotNull null

                // Initialize WorkoutLog
                val workoutLog = WorkoutLog(
                    id = firstItem.workout_log_id,
                    workoutPlanId = firstItem.workout_plan_id,
                    name = firstItem.workout_log_name,
                    startTime = Instant.fromEpochMilliseconds(firstItem.workout_start_time),
                    finishTime = firstItem.workout_finish_time?.let {
                        Instant.fromEpochMilliseconds(it)
                    },
                    note = "",
                )

                // Group items by exercise_id and map them to SetLogs
                val exerciseSessions = items
                    .filter { it.exercise_log_id != null && it.exercise_id != null }
                    .groupBy { it.exercise_log_id }
                    .mapNotNull sessionMap@{ (index, entries) ->
                        // Retrieve first entry for each exercise_id to avoid multiple lookups
                        val entry = entries.firstOrNull() ?: return@sessionMap null

                        // Map each entry to a SetLog, adding previous workout data if available
                        val sets = entries.mapNotNull entries@{ item ->

                            if (item.set_log_id == null
                                || item.exercise_id == null
                                || item.exercise_log_id == null
                            ) {
                                return@entries null
                            }

                            SetLog(
                                id = item.set_log_id,
                                index = item.set_index?.toInt() ?: 0,
                                weight = item.weight?.let { Weight(it.toFloat()) } ?: Weight.ZERO,
                                reps = item.reps?.let { Reps(it.toInt()) } ?: Reps.ZERO,
                                previousWeight = Weight.ZERO,
                                previousReps = Reps.ZERO,
                                isCompleted = if ((item.set_completed ?: 0) == 1L) true else false,
                                exerciseId = item.exercise_log_id,
                                setTypeId = item.set_type_id?.toInt() ?: 2,
                                setPlanId = null,
                            )
                        }

                        ExerciseSession(
                            exerciseLog = ExerciseLog(
                                id = entry.exercise_log_id!!,
                                index = entry.exercise_log_index!!.toInt(),
                                exerciseTemplateId = entry.exercise_id!!,
                                exercisePlanId = entry.exercise_plan_id,
                                note = "",
                                workoutId = workoutLog.id,
                                restDuration = entry.exercise_rest_time_duration!!.milliseconds,
                            ),
                            exercise = ExerciseTemplate(
                                id = entry.exercise_id,
                                primaryMuscle = entry.exercise_primary_muscle!!,
                                secondaryMuscles = entry.exercise_secondary_muscles.orEmpty(),
                                name = entry.exercise_name.orEmpty(),
                                imageUrl = "",
                            ),
                            sets = sets,
                        )
                    }

                WorkoutSession(workoutLog, exerciseSessions)
            }
            .flowOn(dispatchers.io)
    }

    override fun observeWorkoutSessions(): Flow<List<WorkoutSession>> {

        return db.workout_sessionQueries
            .workoutSessions()
            .asFlow()
            .mapToList(dispatchers.io)
            .mapNotNull { items ->

                // Group items by exercise_id and map them to SetLogs
                val sessions = items
                    .groupBy { it.workout_log_id }
                    .mapNotNull workoutMap@{ (_, entries) ->
                        val firstItem = entries.firstOrNull() ?: return@workoutMap null

                        val workoutLog = WorkoutLog(
                            id = firstItem.workout_log_id,
                            workoutPlanId = firstItem.workout_plan_id,
                            name = firstItem.workout_log_name,
                            startTime = Instant.fromEpochMilliseconds(firstItem.workout_start_time),
                            finishTime = firstItem.workout_finish_time?.let {
                                Instant.fromEpochMilliseconds(it)
                            },
                            note = "",
                        )

                        val exerciseSessions = entries
                            .filter { it.exercise_log_id != null && it.exercise_id != null }
                            .groupBy { it.exercise_log_id }
                            .mapNotNull sessionMap@{ (exerciseId, entries) ->
                                // Retrieve first entry for each exercise_id to avoid multiple lookups
                                val entry = entries.firstOrNull() ?: return@sessionMap null

                                // Map each entry to a SetLog, adding previous workout data if available
                                val sets = entries.mapNotNull entries@{ item ->

                                    if (item.set_log_id == null
                                        || item.exercise_id == null
                                        || item.exercise_log_id == null
                                    ) {
                                        return@entries null
                                    }

                                    SetLog(
                                        id = item.set_log_id,
                                        index = item.set_index?.toInt() ?: 0,
                                        weight = item.weight?.let { Weight(it.toFloat()) }
                                            ?: Weight.ZERO,
                                        reps = item.reps?.let { Reps(it.toInt()) } ?: Reps.ZERO,
                                        previousWeight = Weight.ZERO,
                                        previousReps = Reps.ZERO,
                                        isCompleted = if ((item.set_completed
                                                ?: 0) == 1L
                                        ) true else false,
                                        exerciseId = item.exercise_log_id,
                                        setTypeId = item.set_type_id?.toInt() ?: 2,
                                        setPlanId = null,
                                    )
                                }

                                ExerciseSession(
                                    exerciseLog = ExerciseLog(
                                        id = entry.exercise_log_id!!,
                                        index = entry.exercise_log_index!!.toInt(),
                                        exerciseTemplateId = entry.exercise_id!!,
                                        exercisePlanId = entry.exercise_plan_id,
                                        note = "",
                                        workoutId = workoutLog.id,
                                        restDuration = entry.exercise_rest_time_duration!!.milliseconds,
                                    ),
                                    exercise = ExerciseTemplate(
                                        id = entry.exercise_id,
                                        primaryMuscle = entry.exercise_primary_muscle!!,
                                        secondaryMuscles = entry.exercise_secondary_muscles.orEmpty(),
                                        name = entry.exercise_name.orEmpty(),
                                        imageUrl = "",
                                    ),
                                    sets = sets,
                                )
                            }
                        WorkoutSession(
                            workoutLog = workoutLog,
                            exerciseSessions = exerciseSessions,
                        )
                    }

                return@mapNotNull sessions
            }
            .flowOn(dispatchers.io)
    }

    override fun activeWorkout(): Flow<WorkoutSessionOverview?> {
        return db.workout_logQueries.activeWorkoutLogOverview {
                routineId,
                _,
                workoutPlanId,
                name,
                id,
                workoutLogName,
                startTime,
                finishTime,
                restFinishAt,
            ->
            WorkoutSessionOverview(
                routine = WorkoutSessionOverview.RoutineOverview(
                    id = routineId ?: -1,
                    name = "",
                ),
                plan = workoutPlanId?.let {
                    WorkoutSessionOverview.WorkoutPlanOverview(
                        id = workoutPlanId,
                        name = name ?: "Quick Workout",
                    )
                },
                workout = WorkoutLog(
                    id = id,
                    workoutPlanId = workoutPlanId,
                    name = workoutLogName,
                    startTime = Instant.fromEpochMilliseconds(startTime),
                    finishTime = finishTime?.let {
                        Instant.fromEpochMilliseconds(it)
                    },
                    note = "",
                ),
            )
        }
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
    }
}
