package app.shapeshifter.data.db.daos

import app.shapeshifter.data.models.Entity

interface EntityDao<in E : Entity> {

    fun insert(entity: E)

    fun insert(entities: List<E>)

    fun update(entity: E)

    fun upsert(entity: E): Unit = upsert(entity, ::insert, ::update)

    fun upsert(entities: List<E>)

    fun deleteEntity(entity: E)
}

fun <E : Entity> EntityDao<E>.insert(vararg entities: E) = insert(entities.toList())
fun <E : Entity> EntityDao<E>.upsert(vararg entities: E) = upsert(entities.toList())

fun <ET : Entity> upsert(
    entity: ET,
    insert: (ET) -> Unit,
    update: (ET) -> Unit,
    onConflict: ((ET, Throwable) -> Unit)? = null,
) {
    return try {
        if (entity.id != 0L) {
            update(entity)
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
