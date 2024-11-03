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
        workoutPlanId: Long,
        workoutLogId: Long,
    ): Flow<WorkoutSession>

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
                startTime = entity.startTimeInMillis,
                finishTime = entity.finishTimeInMillis,
            )
            db.workout_logQueries.lastInsertRowId().executeAsOne()
        }
    }

    override fun update(entity: WorkoutLog) {
        db.workout_logQueries.update(
            workoutPlanId = entity.workoutPlanId,
            startTime = entity.startTimeInMillis,
            finishTime = entity.finishTimeInMillis,
            id = entity.id,
        )
    }

    override fun deleteEntity(entity: WorkoutLog) {
        db.workout_logQueries.delete(entity.id)
    }

    override fun observeWorkoutWithExercisesAndSets(
        workoutPlanId: Long,
        workoutLogId: Long,
    ): Flow<WorkoutSession> {
        return db.workout_sessionQueries.selectWorkoutSession(
            workoutLogId = workoutLogId,
        )
            .asFlow()
            .mapToList(dispatchers.io)
            .mapNotNull { items ->
                val item = items.firstOrNull() ?: return@mapNotNull null
                val exerciseMap = mutableMapOf<Long, MutableList<SetLog>>()

                val workoutLog = WorkoutLog(
                    id = item.workout_log_id,
                    workoutPlanId = item.workout_plan_id,
                    startTimeInMillis = item.workout_start_time,
                    finishTimeInMillis = item.workout_finish_time,
                    note = "",
                )

                items.forEach { entry ->
                    if (entry.exercise_log_id != null && entry.exercise_id != null) {
                        exerciseMap.getOrPut(entry.exercise_id) { mutableListOf() }.also {
                            if (entry.set_log_id != null) {
                                val previousSet = previousWorkout(
                                    workoutPlanId = entry.workout_plan_id,
                                    exerciseId = entry.exercise_id,
                                    setTypeIndex = entry.set_type_index ?: 0,
                                    currentExerciseLogId = entry.exercise_log_id,
                                )

                                val set = SetLog(
                                    id = entry.set_log_id,
                                    setTypeIndex = PositiveInt(entry.set_type_index?.toInt()!!),
                                    weight = PositiveInt(max(entry.weight?.toInt() ?: 0, 0)),
                                    reps = PositiveInt(max(entry.reps?.toInt() ?: 0, 0)),
                                    prevReps = previousSet?.reps ?: PositiveInt(0),
                                    prevWeight = previousSet?.weight ?: PositiveInt(0),
                                    completed = false,
                                    exerciseLogId = entry.exercise_log_id,
                                    finishTime = entry.set_finish_time ?: 0,
                                    exercisePlanId = entry.exercise_plan_id,
                                    exerciseId = entry.exercise_id,
                                    workoutPlanId = entry.workout_plan_id,
                                    workoutLogId = entry.workout_log_id,
                                )
                                it.add(set)
                            }
                        }
                    }
                }

                val exercises = exerciseMap.entries.mapNotNull { (exerciseId, sets) ->
                    items.find { it.exercise_id == exerciseId }?.let {
                        ExerciseSession(
                            exerciseLog = ExerciseLog(
                                id = it.exercise_log_id!!,
                                exerciseId = it.exercise_id!!,
                                exercisePlanId = it.exercise_plan_id,
                                note = "",
                                workoutLogId = workoutLog.id,
                                workoutPlanId = workoutLog.workoutPlanId,
                            ),
                            exercise = Exercise(
                                id = it.exercise_id,
                                primaryMuscle = it.exercise_primary_muscle!!,
                                secondaryMuscle = it.exercise_secondary_muscles!!,
                                name = it.exercise_name!!,
                                imageUrl = "",
                            ),
                            sets = sets,
                        )
                    }
                }

                WorkoutSession(
                    workoutLog,
                    exercises,
                )
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
                startTime,
                finishTime,
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
                    startTimeInMillis = startTime,
                    finishTimeInMillis = finishTime,
                    note = "",
                ),
            )
        }
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
    }
}
