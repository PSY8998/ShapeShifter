package app.shapeshifter.data.db.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.SelectWorkoutDetails
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.Exercise
import app.shapeshifter.data.models.PositiveInt
import app.shapeshifter.data.models.routines.SavedWorkout
import app.shapeshifter.data.models.workout.Workout
import app.shapeshifter.data.models.workout.WorkoutExercise
import app.shapeshifter.data.models.workout.WorkoutExerciseSet
import app.shapeshifter.data.models.workout.WorkoutExerciseWithSets
import app.shapeshifter.data.models.workout.WorkoutWithExercisesAndSets
import me.tatarka.inject.annotations.Inject
import kotlin.math.max
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext

interface WorkoutEntityDao : EntityDao<Workout> {
    fun observeWorkoutWithExercisesAndSets(workoutId: Long): Flow<WorkoutWithExercisesAndSets>
}

@Inject
class SqlDelightWorkoutEntityDao(
    override val db: ShapeShifterDatabase,
    private val dispatchers: AppCoroutineDispatchers,
) : SqlDelightEntityDao<Workout>, WorkoutEntityDao {
    override fun insert(entity: Workout) {
        db.workoutQueries.insert(
            id = entity.id,
            saved_workout_id = entity.savedWorkoutId,
            start_time = entity.startTimeInMillis,
            finish_time = entity.finishTimeInMillis,
        )
    }

    override fun update(entity: Workout) {
        TODO("Not yet implemented")
    }

    override fun deleteEntity(entity: Workout) {
        TODO("Not yet implemented")
    }

    override fun observeWorkoutWithExercisesAndSets(workoutId: Long): Flow<WorkoutWithExercisesAndSets> {
        return db.workout_detailsQueries.selectWorkoutDetails(workoutId)
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
                    entry.workout_exercise_set_id ?: return@forEach
                    val set = WorkoutExerciseSet(
                        id = entry.workout_exercise_set_id,
                        index = PositiveInt(0), // Consider implications of always setting to 0
                        weight = PositiveInt(max(entry.weight?.toInt() ?: 0, 0)),
                        reps = PositiveInt(max(entry.reps?.toInt() ?: 0, 0)),
                        completed = false,
                    )
                    exerciseMap.getOrPut(entry.exercise_id) { mutableListOf() }.add(set)
                }

                val exercises = exerciseMap.entries.mapNotNull { (exerciseId, sets) ->
                    items.find { it.exercise_id == exerciseId }?.let {
                        WorkoutExerciseWithSets(
                            id = it.workout_exercise_id,
                            exercise = Exercise(
                                id = it.exercise_id,
                                primaryMuscle = it.exercise_primary_muscle,
                                secondaryMuscle = it.exercise_secondary_muscles,
                                name = it.exercise_name,
                                imageUrl = "",
                            ),
                            sets = sets,
                        )
                    }
                }

                WorkoutWithExercisesAndSets(workout, exercises)
            }
    }

}
