package app.shapeshifter.data.db.daos

import app.shapeshifter.data.models.Entity

interface EntityDao<in E : Entity> {

    fun insert(entity: E): Long

    fun insert(entities: List<E>)

    fun update(entity: E)

    fun upsert(entity: E): Long = upsert(entity, ::insert, ::update)

    fun upsert(entities: List<E>)

    fun deleteEntity(entity: E)
}

fun <E : Entity> EntityDao<E>.insert(vararg entities: E) = insert(entities.toList())
fun <E : Entity> EntityDao<E>.upsert(vararg entities: E) = upsert(entities.toList())

fun <ET : Entity> upsert(
    entity: ET,
    insert: (ET) -> Long,
    update: (ET) -> Unit,
    onConflict: ((ET, Throwable) -> Long)? = null,
): Long {
    return try {
        if (entity.id != 0L) {
            update(entity)
            entity.id
        } else {
            insert(entity)
        }
    } catch (t: Throwable) {
        when {
            onConflict != null -> onConflict(entity, t)
            else -> throw t
        }
    }
}
