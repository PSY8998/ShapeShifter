package app.shapeshifter.data.db

import app.cash.sqldelight.db.SqlDriver
import app.shapeshifter.core.base.inject.ApplicationScope
import app.shapeshifter.data.db.columnadapters.MuscleColumnAdapter
import app.shapeshifter.data.db.columnadapters.MusclesColumnAdapter
import app.shapeshifter.data.db.daos.ExerciseEntityDao
import app.shapeshifter.data.db.daos.RoutineEntityDao
import app.shapeshifter.data.db.daos.SqlDelightExerciseEntityDao
import app.shapeshifter.data.db.daos.SqlDelightRoutineEntityDao
import app.shapeshifter.data.db.daos.SqlDelightWorkoutEntityDao
import app.shapeshifter.data.db.daos.SqlDelightWorkoutExerciseEntityDao
import app.shapeshifter.data.db.daos.SqlDelightWorkoutExerciseSetEntityDao
import app.shapeshifter.data.db.daos.WorkoutEntityDao
import app.shapeshifter.data.db.daos.WorkoutExerciseEntityDao
import app.shapeshifter.data.db.daos.WorkoutExerciseSetEntityDao
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
            secondary_musclesAdapter = MusclesColumnAdapter,
        ),
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
    ): DatabaseTransactionRunner = runner

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

    @ApplicationScope
    @Provides
    fun provideWorkoutEntityDao(
        dao: SqlDelightWorkoutEntityDao,
    ): WorkoutEntityDao = dao

    @ApplicationScope
    @Provides
    fun provideWorkoutExerciseEntityDao(
        dao: SqlDelightWorkoutExerciseEntityDao,
    ): WorkoutExerciseEntityDao = dao

    @ApplicationScope
    @Provides
    fun provideWorkoutExerciseSetEntityDao(
        dao: SqlDelightWorkoutExerciseSetEntityDao
    ) : WorkoutExerciseSetEntityDao = dao
}
