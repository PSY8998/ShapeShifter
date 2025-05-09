package app.shapeshifter.data.db.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.ExerciseTemplate
import app.shapeshifter.data.models.plans.ExercisePlanSession
import app.shapeshifter.data.models.plans.WorkoutPlanSession
import app.shapeshifter.data.models.workout.ExercisePlan
import app.shapeshifter.data.models.workout.Reps
import app.shapeshifter.data.models.workout.SetPlan
import app.shapeshifter.data.models.workout.Weight
import app.shapeshifter.data.models.workout.WorkoutPlan
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull

@Inject
class SqlDelightWorkoutPlanEntityDao(
    val db: ShapeShifterDatabase,
    private val transactionRunner: DatabaseTransactionRunner,
    private val dispatchers: AppCoroutineDispatchers,
) {

    fun updateOrInsert(entity: WorkoutPlan): Long {
        return if (entity.id.toInt() == 0) {
            insert(entity)
        } else {
            update(entity)
            entity.id
        }
    }

    fun insert(entity: WorkoutPlan): Long {
        return transactionRunner {
            db.workout_planQueries.insert(
                id = entity.id,
                routineId = entity.routineId,
                name = entity.name,
                note = entity.note,
            )
            db.workout_planQueries.lastInsertRowId().executeAsOne()
        }
    }

    fun update(entity: WorkoutPlan) {
        db.workout_planQueries.update(
            routineId = entity.routineId,
            name = entity.name,
            id = entity.id,
            note = entity.note,
        )
    }

    fun deleteEntity(entity: WorkoutPlan) {
        db.workout_planQueries.delete(entity.id)
    }

    fun observeWorkoutPlanSessions(): Flow<List<WorkoutPlanSession>> {
        return db.workout_plan_sessionQueries
            .observeWorkoutSessions()
            .asFlow()
            .mapToList(dispatchers.io)
            .mapNotNull { items ->
                val sessions = items
                    .groupBy { it.workout_plan_id }
                    .mapNotNull workoutMap@{ (_, entries) ->
                        val firstItem = entries.firstOrNull() ?: return@workoutMap null

                        val workoutPlan = WorkoutPlan(
                            id = firstItem.workout_plan_id,
                            routineId = firstItem.workout_plan_routine_id,
                            name = firstItem.workout_plan_name,
                            note = null,
                        )

                        val exercisePlanSessions = entries
                            .filter { it.exercise_plan_id != null && it.exercise_id != null }
                            .groupBy { it.exercise_plan_id }
                            .mapNotNull sessionMap@{ (exerciseId, entries) ->

                                val entry = entries.firstOrNull() ?: return@sessionMap null

                                val sets = entries.mapNotNull entries@{ item ->

                                    if (item.set_plan_id == null
                                        || item.exercise_id == null
                                        || item.exercise_plan_id == null
                                    ) {
                                        return@entries null
                                    }

                                    SetPlan(
                                        id = item.set_plan_id,
                                        exerciseId = item.exercise_plan_id,
                                        index = 0,
                                        weight = Weight(item.weight!!.toFloat()),
                                        reps = Reps(item.reps!!.toInt()),
                                        setTypeId = 2,
                                    )
                                }

                                ExercisePlanSession(
                                    exercisePlan = ExercisePlan(
                                        id = entry.exercise_plan_id!!,
                                        workoutId = entry.workout_plan_id,
                                        exerciseTemplateId = entry.exercise_id!!,
                                        index = 0,
                                        note = null,
                                        restDuration = null,
                                    ),
                                    exercise = ExerciseTemplate(
                                        id = exerciseId!!,
                                        name = entry.exercise_name.orEmpty(),
                                        primaryMuscle = entry.exercise_primary_muscle!!,
                                        secondaryMuscles = entry.exercise_secondary_muscles.orEmpty(),
                                        imageUrl = "",
                                    ),
                                    setPlans = sets,
                                )
                            }

                        WorkoutPlanSession(
                            workoutPlan = workoutPlan,
                            exercisePlanSessions = exercisePlanSessions,
                        )
                    }
                return@mapNotNull sessions
            }
            .flowOn(dispatchers.io)
    }

    fun workoutPlanSession(workoutPlanId: Long): WorkoutPlanSession {

        val session = db.workout_plan_sessionQueries
            .selectWorkoutPlanSession(workoutPlanId)
            .executeAsList()
        val exercisePlanSessions = session
            .groupBy { it.exercise_plan_id }.map { (_, entries) ->
                val entry = entries.first()

                val setPlans = entries.mapNotNull { session ->
                    session.set_plan_id?.let {
                        SetPlan(
                            id = session.set_plan_id,
                            reps = Reps(session.reps!!.toInt()),
                            weight = Weight(session.weight!!.toFloat()),
                            exerciseId = session.exercise_plan_id!!,
                            index = 0,
                            setTypeId = 2,
                        )
                    }
                }
                ExercisePlanSession(
                    exercisePlan = ExercisePlan(
                        id = entry.exercise_plan_id!!,
                        workoutId = entry.workout_plan_id,
                        exerciseTemplateId = entry.exercise_id!!,
                        index = 0,
                        note = null,
                        restDuration = null,
                    ),
                    exercise = ExerciseTemplate(
                        id = entry.exercise_id,
                        name = entry.exercise_name.orEmpty(),
                        primaryMuscle = entry.exercise_primary_muscle!!,
                        secondaryMuscles = entry.exercise_secondary_muscles.orEmpty(),
                        imageUrl = "",
                    ),
                    setPlans = setPlans,
                )
            }

        val entry = session.first()

        return WorkoutPlanSession(
            workoutPlan = WorkoutPlan(
                id = entry.workout_plan_id,
                routineId = entry.workout_plan_routine_id,
                name = entry.workout_plan_name,
                note = null,
            ),
            exercisePlanSessions = exercisePlanSessions,
        )
    }
}

