package app.shapeshifter.inject

import android.app.Application
import app.shapeshifter.common.imageloading.ImageLoadingComponent
import app.shapeshifter.core.base.inject.AppCoroutineDispatchers
import app.shapeshifter.core.base.inject.ApplicationCoroutineScope
import app.shapeshifter.core.base.inject.ApplicationScope
import app.shapeshifter.data.db.SqlDelightDatabaseComponent
import app.shapeshifter.data.supabase.SupabaseComponent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob

@ApplicationScope
@Component
abstract class ApplicationComponent(
    @get:Provides val application: Application,
) : SqlDelightDatabaseComponent,
    SupabaseComponent,
    ImageLoadingComponent {

    @OptIn(ExperimentalCoroutinesApi::class)
    @ApplicationScope
    @Provides
    fun provideCoroutineDispatchers(): AppCoroutineDispatchers = AppCoroutineDispatchers(
        io = Dispatchers.IO,
        databaseWrite = Dispatchers.IO.limitedParallelism(1),
        databaseRead = Dispatchers.IO.limitedParallelism(4),
        computation = Dispatchers.Default,
        main = Dispatchers.Main,
    )

    @ApplicationScope
    @Provides
    fun provideApplicationCoroutineScope(
        dispatchers: AppCoroutineDispatchers,
    ): ApplicationCoroutineScope = CoroutineScope(dispatchers.main + SupervisorJob())

    companion object
}
