package app.shapeshifter.data.db.daos

import app.shapeshifter.data.models.Entity
import app.shapeshifter.data.db.ShapeShifterDatabase

interface SqlDelightEntityDao<in E : Entity> : EntityDao<E> {
    val db: ShapeShifterDatabase

    override fun insert(entities: List<E>) {
        db.transaction {
            for (entity in entities) {
                insert(entity)
            }
        }
    }

    override fun upsert(entities: List<E>) {
        db.transaction {
            for (entity in entities) {
                upsert(entity)
            }
        }
    }
}
