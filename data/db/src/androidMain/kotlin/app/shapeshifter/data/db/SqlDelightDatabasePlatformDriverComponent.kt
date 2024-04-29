package app.shapeshifter.data.db

import android.app.Application
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.shapeshifter.core.base.inject.ApplicationScope
import me.tatarka.inject.annotations.Provides

actual interface SqlDelightDatabasePlatformDriverComponent {
    @ApplicationScope
    @Provides
    fun provideDatabaseDriver(
        application: Application,
    ): SqlDriver = AndroidSqliteDriver(
        schema = ShapeShifterDatabase.Schema,
        context = application,
        name = "shapeshifter.db",
    )
}
