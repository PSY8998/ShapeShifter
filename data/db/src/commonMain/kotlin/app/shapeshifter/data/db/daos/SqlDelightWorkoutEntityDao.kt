package app.shapeshifter.data.db.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.Exercise
import app.shapeshifter.data.models.PositiveInt
import app.shapeshifter.data.models.workout.Workout
import app.shapeshifter.data.models.workout.WorkoutExerciseSet
import app.shapeshifter.data.models.workout.WorkoutExerciseWithSets
import app.shapeshifter.data.models.workout.WorkoutWithExercisesAndSets
import app.shapeshifter.data.models.workout.WorkoutWithSaveInfo
import me.tatarka.inject.annotations.Inject
import kotlin.math.max
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

interface WorkoutEntityDao : EntityDao<Workout> {
    fun observeWorkoutWithExercisesAndSets(workoutId: Long): Flow<WorkoutWithExercisesAndSets>

    fun activeWorkout(): Flow<Workout?>
}

@Inject
class SqlDelightWorkoutEntityDao(
    override val db: ShapeShifterDatabase,
    private val transactionRunner: DatabaseTransactionRunner,
    private val dispatchers: AppCoroutineDispatchers,
) : SqlDelightEntityDao<Workout>, WorkoutEntityDao {
    override fun insert(entity: Workout): Long {
        return transactionRunner {
            db.workout_logQueries.insert(
                id = entity.id,
                savedWorkoutId = entity.savedWorkoutId,
                startTime = entity.startTimeInMillis,
                finishTime = entity.finishTimeInMillis,
            )
            db.workout_logQueries.lastInsertRowId().executeAsOne()
        }
    }

    override fun update(entity: Workout) {
        TODO("Not yet implemented")
    }

    override fun deleteEntity(entity: Workout) {
        db.workout_logQueries.delete(entity.id)
    }

    override fun observeWorkoutWithExercisesAndSets(
        workoutId: Long,
    ): Flow<WorkoutWithExercisesAndSets> {
        return db.workout_sessionQueries.selectWorkoutSession(workoutId)
            .asFlow()
            .mapToList(dispatchers.io)
            .mapNotNull { items ->
                val item = items.firstOrNull() ?: return@mapNotNull null
                val exerciseMap = mutableMapOf<Long, MutableList<WorkoutExerciseSet>>()

                val workout = Workout(
                    id = item.workout_id,
                    savedWorkoutId = item.saved_workout_id,
                    startTimeInMillis = item.workout_start_time,
                    finishTimeInMillis = item.workout_finish_time,
                    note = "",
                )

                items.forEach { entry ->
                    if (entry.exercise_id != null) {
                        exerciseMap.getOrPut(entry.exercise_id) { mutableListOf() }.also {
                            if (entry.workout_exercise_set_id != null) {
                                val set = WorkoutExerciseSet(
                                    id = entry.workout_exercise_set_id,
                                    index = PositiveInt(0),
                                    weight = PositiveInt(max(entry.weight?.toInt() ?: 0, 0)),
                                    reps = PositiveInt(max(entry.reps?.toInt() ?: 0, 0)),
                                    completed = false,
                                )
                                it.add(set)
                            }
                        }
                    }
                }

                val exercises = exerciseMap.entries.mapNotNull { (exerciseId, sets) ->
                    items.find { it.exercise_id == exerciseId }?.let {
                        WorkoutExerciseWithSets(
                            id = it.workout_exercise_id!!,
                            exercise = Exercise(
                                id = it.exercise_id!!,
                                primaryMuscle = it.exercise_primary_muscle!!,
                                secondaryMuscle = it.exercise_secondary_muscles!!,
                                name = it.exercise_name!!,
                                imageUrl = "",
                            ),
                            sets = sets,
                        )
                    }
                }

                WorkoutWithExercisesAndSets(workout, exercises)
            }
    }

    override fun activeWorkout(): Flow<Workout?> {
        return db.workout_logQueries.activeWorkoutOverview(
            mapper = { id, startTime, name ->
                Workout(

                )
            },
        )
            .asFlow()
            .mapToOne(dispatchers.io)
    }
}
