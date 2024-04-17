package app.shapeshifter.database

import android.app.Application
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.shapeshifter.ShapeShifterDatabase
import app.shapeshifter.feature.exercise.data.ShapeShifterDatabase as ExerciseDatabase
import app.shapeshifter.core.base.inject.ApplicationScope
import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.annotations.Provides

interface SqlDelightDatabaseDriverComponent {
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

@Inject
class DatabaseFactory(
    private val driver: SqlDriver,
) {
    fun build() = ShapeShifterDatabase(driver)
}

interface SqlDelightDatabaseComponent : SqlDelightDatabaseDriverComponent {
    @ApplicationScope
    @Provides
    fun provideSqlDelightDatabase(
        factory: DatabaseFactory,
    ): ShapeShifterDatabase = factory.build()

    @ApplicationScope
    @Provides
    fun provideExerciseDatabase(
        database: ShapeShifterDatabase,
    ): ExerciseDatabase = database
}
