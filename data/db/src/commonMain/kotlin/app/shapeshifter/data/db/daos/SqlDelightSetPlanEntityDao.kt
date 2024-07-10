package app.shapeshifter.data.db.daos

import app.shapeshifter.data.db.ShapeShifterDatabase
import app.shapeshifter.data.models.plans.SetPlan

class SqlDelightSetPlanEntityDao(
    override val db: ShapeShifterDatabase,
) : SqlDelightEntityDao<SetPlan> {
    override fun insert(entity: SetPlan): Long {
        TODO("Not yet implemented")
    }

    override fun update(entity: SetPlan) {
        TODO("Not yet implemented")
    }

    override fun deleteEntity(entity: SetPlan) {
        TODO("Not yet implemented")
    }

}
