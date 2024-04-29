package app.shapeshifter.data.db

import app.cash.sqldelight.db.SqlDriver
import app.shapeshifter.core.base.inject.ApplicationScope
import app.shapeshifter.data.db.columnadapters.MuscleColumnAdapter
import app.shapeshifter.data.db.columnadapters.MusclesColumnAdapter
import app.shapeshifter.data.db.daos.ExerciseEntityDao
import app.shapeshifter.data.db.daos.RoutineEntityDao
import app.shapeshifter.data.db.daos.SqlDelightExerciseEntityDao
import app.shapeshifter.data.db.daos.SqlDelightRoutineEntityDao
import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.annotations.Provides

expect interface SqlDelightDatabasePlatformDriverComponent

@Inject
class DatabaseFactory(
    private val driver: SqlDriver,
) {
    fun build() = ShapeShifterDatabase(
        driver = driver,
        exerciseAdapter = Exercise.Adapter(
            primary_muscleAdapter = MuscleColumnAdapter,
            secondary_musclesAdapter = MusclesColumnAdapter
        )
    )
}

interface SqlDelightDatabaseComponent : SqlDelightDatabasePlatformDriverComponent {
    @ApplicationScope
    @Provides
    fun provideSqlDelightDatabase(
        factory: DatabaseFactory,
    ): ShapeShifterDatabase = factory.build()

    @ApplicationScope
    @Provides
    fun provideDatabaseTransactionRunner(
        runner: SqlDelightTransactionRunner,
    ) = runner

    @ApplicationScope
    @Provides
    fun provideExerciseEntityDao(
        dao: SqlDelightExerciseEntityDao,
    ): ExerciseEntityDao = dao

    @ApplicationScope
    @Provides
    fun provideRoutineEntityDao(
        dao: SqlDelightRoutineEntityDao,
    ): RoutineEntityDao = dao
}
