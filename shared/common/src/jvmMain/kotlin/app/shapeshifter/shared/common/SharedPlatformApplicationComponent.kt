package app.shapeshifter.shared.common

import app.shapeshifter.core.base.inject.CachePath
import app.shapeshifter.dara.datastore.DataStoreComponent
import me.tatarka.inject.annotations.Provides

actual interface SharedPlatformApplicationComponent :
    DataStoreComponent {
    @Provides
    @CachePath
    fun provideCachePath(): String = getCacheDir().absolutePath
}
