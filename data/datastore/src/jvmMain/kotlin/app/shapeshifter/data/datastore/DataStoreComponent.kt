package app.shapeshifter.data.datastore

import app.shapeshifter.core.base.inject.ApplicationScope
import app.shapeshifter.core.base.inject.CachePath
import app.shapeshifter.core.base.inject.DataStorePath
import me.tatarka.inject.annotations.Provides
import java.io.File

actual interface DataStoreComponent {
    @Provides
    @DataStorePath
    @ApplicationScope
    fun provideDataStore(
        @CachePath cachePath: String
    ): (name: String) -> String {
        return { name ->
            File(cachePath).resolve(name).absolutePath
        }
    }
}
