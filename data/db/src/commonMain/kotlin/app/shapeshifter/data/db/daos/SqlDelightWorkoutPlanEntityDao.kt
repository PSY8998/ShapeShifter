package app.shapeshifter.data.db.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.data.db.DatabaseTransactionRunner
import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.Exercise
import app.shapeshifter.data.models.PositiveInt
import app.shapeshifter.data.models.plans.ExercisePlan
import app.shapeshifter.data.models.plans.ExercisePlanSession
import app.shapeshifter.data.models.plans.SetPlan
import app.shapeshifter.data.models.plans.WorkoutPlan
import app.shapeshifter.data.models.plans.WorkoutPlanSession
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull

@Inject
class SqlDelightWorkoutPlanEntityDao(
    override val db: ShapeShifterDatabase,
    private val transactionRunner: DatabaseTransactionRunner,
    private val dispatchers: AppCoroutineDispatchers,
) : SqlDelightEntityDao<WorkoutPlan> {
    override fun insert(entity: WorkoutPlan): Long {
        return transactionRunner {
            db.workout_planQueries.insert(
                id = entity.id,
                routineId = entity.routineId,
                name = entity.name,
            )
            db.workout_planQueries.lastInsertRowId().executeAsOne()
        }
    }

    override fun update(entity: WorkoutPlan) {
        TODO("Not yet implemented")
    }

    override fun deleteEntity(entity: WorkoutPlan) {
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
                                        exercisePlanId = item.exercise_plan_id,
                                        index = PositiveInt(0),
                                        weight = item.weight!!.toInt(),
                                        reps = item.reps!!.toInt(),
                                    )
                                }

                                ExercisePlanSession(
                                    exercisePlan = ExercisePlan(
                                        id = entry.exercise_plan_id!!,
                                        workoutPlanId = entry.workout_plan_id,
                                        exerciseId = entry.exercise_id!!,
                                        index = PositiveInt(0),
                                    ),
                                    exercise = Exercise(
                                        id = exerciseId!!,
                                        name = entry.exercise_name.orEmpty(),
                                        primaryMuscle = entry.exercise_primary_muscle!!,
                                        secondaryMuscle = entry.exercise_secondary_muscles.orEmpty(),
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

    fun workoutPlanSession(workoutPlanId: Long): WorkoutPlanSession{

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
                        reps = session.reps!!.toInt(),
                        weight = session.weight!!.toInt(),
                        exercisePlanId = session.exercise_plan_id!!,
                        index = PositiveInt(0),
                    )
                }
            }
            ExercisePlanSession(
                exercisePlan = ExercisePlan(
                    id = entry.exercise_plan_id!!,
                    workoutPlanId = entry.workout_plan_id,
                    exerciseId = entry.exercise_id!!,
                    index = PositiveInt(0),
                ),
                exercise = Exercise(
                    id = entry.exercise_id,
                    name = entry.exercise_name.orEmpty(),
                    primaryMuscle = entry.exercise_primary_muscle!!,
                    secondaryMuscle = entry.exercise_secondary_muscles.orEmpty(),
                    imageUrl = "",
                ),
                setPlans = setPlans
            )
        }

        val entry = session.first()

        return WorkoutPlanSession(
            workoutPlan = WorkoutPlan(
                id = entry.workout_plan_id,
                routineId = entry.workout_plan_routine_id,
                name = entry.workout_plan_name
            ),
            exercisePlanSessions = exercisePlanSessions
        )
    }
}

