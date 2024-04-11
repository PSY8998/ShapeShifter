package app.shapeshifter.feature.home.ui.sqldelight.data

import android.app.Application
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.shapeshifter.core.base.inject.ApplicationScope
import app.shapeshifter.feature.home.ui.Database
import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.annotations.Provides

interface SqlDelightDatabaseDriverComponent {
    @ApplicationScope
    @Provides
    fun provideDatabaseDriver(
        application: Application,
    ) = AndroidSqliteDriver(
        schema = Database.Schema,
        context = application,
        name = "exercise.db",
    )
}

@Inject
class DatabaseFactory(
    private val driver: SqlDriver,
) {
    fun build(): Database = Database(driver)
}

interface SqlDelightDatabaseComponent : SqlDelightDatabaseDriverComponent {
    @ApplicationScope
    @Provides
    fun provideSqlDelightDatabase(
        factory: DatabaseFactory,
    ) = factory.build()
}
