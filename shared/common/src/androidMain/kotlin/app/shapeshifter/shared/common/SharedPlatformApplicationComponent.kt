package app.shapeshifter.shared.common

import android.app.Application
import android.content.Context
import app.shapeshifter.core.base.inject.ApplicationScope
import app.shapeshifter.core.base.inject.CachePath
import app.shapeshifter.data.datastore.createDataStore.DataStoreComponent
import me.tatarka.inject.annotations.Provides

actual interface SharedPlatformApplicationComponent :
    DataStoreComponent {

    @ApplicationScope
    @Provides
    fun provideApplicationContext(
        application: Application,
    ): Context = application

    @Provides
    @CachePath
    fun provideCachePath(
        application: Application,
    ): String = application.cacheDir.absolutePath
}
