package app.shapeshifter.data.db.daos

import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.plans.ExercisePlan

class SqlDelightExercisePlanEntityDao(
    override val db: ShapeShifterDatabase,
) : SqlDelightEntityDao<ExercisePlan> {
    override fun insert(entity: ExercisePlan): Long {
        TODO("Not yet implemented")
    }

    override fun update(entity: ExercisePlan) {
        TODO("Not yet implemented")
    }

    override fun deleteEntity(entity: ExercisePlan) {
        TODO("Not yet implemented")
    }

}
