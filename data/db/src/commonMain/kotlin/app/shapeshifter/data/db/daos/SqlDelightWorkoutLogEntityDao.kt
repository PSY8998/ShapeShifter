package app.shapeshifter.data.db.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.Exercise
import app.shapeshifter.data.models.PositiveInt
import app.shapeshifter.data.models.workoutlog.ExerciseLog
import app.shapeshifter.data.models.workoutlog.ExerciseSession
import app.shapeshifter.data.models.workoutlog.WorkoutLog
import app.shapeshifter.data.models.workoutlog.SetLog
import app.shapeshifter.data.models.workoutlog.WorkoutSession
import app.shapeshifter.data.models.workoutlog.WorkoutSessionOverview
import me.tatarka.inject.annotations.Inject
import kotlin.math.max
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull

interface WorkoutEntityDao : EntityDao<WorkoutLog> {
    fun observeWorkoutWithExercisesAndSets(
        workoutLogId: Long,
    ): Flow<WorkoutSession>

    fun observeWorkoutSessions(): Flow<List<WorkoutSession>>

    fun activeWorkout(): Flow<WorkoutSessionOverview?>

}

@Inject
class SqlDelightWorkoutEntityDao(
    override val db: ShapeShifterDatabase,
    private val transactionRunner: DatabaseTransactionRunner,
    private val dispatchers: AppCoroutineDispatchers,
) : SqlDelightEntityDao<WorkoutLog>, WorkoutEntityDao {
    override fun insert(entity: WorkoutLog): Long {
        return transactionRunner {
            db.workout_logQueries.insert(
                id = entity.id,
                workoutPlanId = entity.workoutPlanId,
                name = entity.name,
                startTime = entity.startTimeInMillis,
                finishTime = entity.finishTimeInMillis,
                restFinishAt = entity.restFinishTimeInMillis,
            )
            db.workout_logQueries.lastInsertRowId().executeAsOne()
        }
    }

    override fun update(entity: WorkoutLog) {
        db.workout_logQueries.update(
            workoutPlanId = entity.workoutPlanId,
            name = entity.name,
            startTime = entity.startTimeInMillis,
            finishTime = entity.finishTimeInMillis,
            id = entity.id,
            restFinishAt = entity.restFinishTimeInMillis,
        )
    }

    override fun deleteEntity(entity: WorkoutLog) {
        db.workout_logQueries.delete(entity.id)
    }

    override fun observeWorkoutWithExercisesAndSets(
        workoutLogId: Long,
    ): Flow<WorkoutSession> {
        // Cache for previousWorkout results
        val previousWorkoutCache = mutableMapOf<Triple<Long, Long, Long>, SetLog?>()

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
                    startTimeInMillis = firstItem.workout_start_time,
                    finishTimeInMillis = firstItem.workout_finish_time,
                    note = "",
                    restFinishTimeInMillis = firstItem.workout_rest_finish_at,
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

                            val cacheKey = Triple(
                                item.workout_plan_id,
                                item.exercise_id,
                                item.set_type_index ?: 0,
                            )

                            // Retrieve previous set from cache or compute if not cached
                            val previousSet = previousWorkoutCache.getOrPut(cacheKey) {
                                previousWorkout(
                                    workoutPlanId = item.workout_plan_id,
                                    exerciseId = item.exercise_id,
                                    setTypeIndex = item.set_type_index ?: 0,
                                    currentExerciseLogId = item.exercise_log_id,
                                )
                            }

                            SetLog(
                                id = item.set_log_id,
                                setTypeIndex = PositiveInt(item.set_type_index?.toInt() ?: 0),
                                weight = PositiveInt(max(item.weight?.toInt() ?: 0, 0)),
                                reps = PositiveInt(max(item.reps?.toInt() ?: 0, 0)),
                                prevReps = previousSet?.reps ?: PositiveInt(0),
                                prevWeight = previousSet?.weight ?: PositiveInt(0),
                                completed = false,
                                exerciseLogId = item.exercise_log_id,
                                finishTime = item.set_finish_time ?: 0,
                                exercisePlanId = item.exercise_plan_id,
                                exerciseId = item.exercise_id,
                                workoutPlanId = item.workout_plan_id,
                                workoutLogId = item.workout_log_id,
                            )
                        }

                        ExerciseSession(
                            exerciseLog = ExerciseLog(
                                id = entry.exercise_log_id!!,
                                index = entry.exercise_log_index!!,
                                exerciseId = entry.exercise_id!!,
                                exercisePlanId = entry.exercise_plan_id,
                                note = "",
                                workoutLogId = workoutLog.id,
                                workoutPlanId = workoutLog.workoutPlanId,
                                restTimeDuration = entry.exercise_rest_time_duration!!,
                            ),
                            exercise = Exercise(
                                id = entry.exercise_id,
                                primaryMuscle = entry.exercise_primary_muscle!!,
                                secondaryMuscle = entry.exercise_secondary_muscles.orEmpty(),
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
        val previousWorkoutCache = mutableMapOf<Triple<Long, Long, Long>, SetLog?>()

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
                            startTimeInMillis = firstItem.workout_start_time,
                            finishTimeInMillis = firstItem.workout_finish_time,
                            note = "",
                            restFinishTimeInMillis = firstItem.workout_rest_finish_at,
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

                                    val cacheKey = Triple(
                                        item.workout_plan_id,
                                        item.exercise_id,
                                        item.set_type_index ?: 0,
                                    )

                                    // Retrieve previous set from cache or compute if not cached
                                    val previousSet = previousWorkoutCache.getOrPut(cacheKey) {
                                        previousWorkout(
                                            workoutPlanId = item.workout_plan_id,
                                            exerciseId = item.exercise_id,
                                            setTypeIndex = item.set_type_index ?: 0,
                                            currentExerciseLogId = item.exercise_log_id,
                                        )
                                    }

                                    SetLog(
                                        id = item.set_log_id,
                                        setTypeIndex = PositiveInt(
                                            item.set_type_index?.toInt() ?: 0,
                                        ),
                                        weight = PositiveInt(max(item.weight?.toInt() ?: 0, 0)),
                                        reps = PositiveInt(max(item.reps?.toInt() ?: 0, 0)),
                                        prevReps = previousSet?.reps ?: PositiveInt(0),
                                        prevWeight = previousSet?.weight ?: PositiveInt(0),
                                        completed = false,
                                        exerciseLogId = item.exercise_log_id,
                                        finishTime = item.set_finish_time ?: 0,
                                        exercisePlanId = item.exercise_plan_id,
                                        exerciseId = item.exercise_id,
                                        workoutPlanId = item.workout_plan_id,
                                        workoutLogId = item.workout_log_id,
                                    )
                                }

                                ExerciseSession(
                                    exerciseLog = ExerciseLog(
                                        id = entry.exercise_log_id!!,
                                        index = entry.exercise_log_index!!,
                                        exerciseId = exerciseId!!,
                                        exercisePlanId = entry.exercise_plan_id,
                                        restTimeDuration = entry.exercise_rest_time_duration!!,
                                        note = "",
                                        workoutLogId = workoutLog.id,
                                        workoutPlanId = workoutLog.workoutPlanId,
                                    ),
                                    exercise = Exercise(
                                        id = exerciseId,
                                        primaryMuscle = entry.exercise_primary_muscle!!,
                                        secondaryMuscle = entry.exercise_secondary_muscles.orEmpty(),
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

    private fun previousWorkout(
        workoutPlanId: Long,
        exerciseId: Long,
        currentExerciseLogId: Long,
        setTypeIndex: Long,
    ): SetLog? {
        val previousSetForWorkout = db.set_logQueries
            .previousExerciseSetForWorkout(
                workoutPlanId = workoutPlanId,
                exerciseId = exerciseId,
                setTypeIndex = setTypeIndex,
                currentExerciseLogId = currentExerciseLogId,
            ).executeAsOneOrNull()
            ?: db.set_logQueries
                .previousExerciseSetForIndex(
                    exerciseId = exerciseId,
                    setTypeIndex = setTypeIndex,
                    currentExerciseLogId = currentExerciseLogId,
                ).executeAsOneOrNull()
            ?: db.set_logQueries
                .previousExerciseSetWithHighestIndex(
                    exerciseId = exerciseId,
                    currentExerciseLogId = currentExerciseLogId,
                )
                .executeAsOneOrNull()
            ?: return null

        return SetLog(
            id = previousSetForWorkout.id,
            setTypeIndex = PositiveInt(previousSetForWorkout.set_type_index.toInt()),
            exerciseLogId = previousSetForWorkout.exercise_log_id,
            exercisePlanId = previousSetForWorkout.exercise_plan_id,
            exerciseId = previousSetForWorkout.exercise_id,
            workoutPlanId = previousSetForWorkout.workout_plan_id,
            workoutLogId = previousSetForWorkout.workout_log_id,
            weight = PositiveInt(previousSetForWorkout.weight.toInt()),
            reps = PositiveInt(previousSetForWorkout.reps.toInt()),
            prevReps = PositiveInt(0),
            prevWeight = PositiveInt(0),
            completed = true,
            finishTime = 0,
        )

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
                plan = WorkoutSessionOverview.WorkoutPlanOverview(
                    id = workoutPlanId,
                    name = name ?: "Quick Workout",
                ),
                workout = WorkoutLog(
                    id = id,
                    workoutPlanId = workoutPlanId,
                    name = workoutLogName,
                    startTimeInMillis = startTime,
                    finishTimeInMillis = finishTime,
                    note = "",
                    restFinishTimeInMillis = restFinishAt,
                ),
            )
        }
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
    }
}
