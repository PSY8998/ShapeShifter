package app.shapeshifter.data.db

import me.tatarka.inject.annotations.Inject

interface DatabaseTransactionRunner {
    operator fun <T> invoke(block: () -> T): T
}

@Inject
class SqlDelightTransactionRunner(
    private val db: ShapeShifterDatabase,
) : DatabaseTransactionRunner {
    override fun <T> invoke(block: () -> T): T {
        return db.transactionWithResult {
            block()
        }
    }
}
