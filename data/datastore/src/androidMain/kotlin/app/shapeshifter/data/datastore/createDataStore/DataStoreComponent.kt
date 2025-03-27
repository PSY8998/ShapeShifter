package app.shapeshifter.data.datastore.createDataStore

import android.content.Context
import app.shapeshifter.core.base.inject.ApplicationScope
import app.shapeshifter.core.base.inject.DataStorePath
import me.tatarka.inject.annotations.Provides

interface DataStoreComponent {
    @Provides
    @DataStorePath
    @ApplicationScope
    fun provideDataStore(
        context: Context,
    ): (name: String) -> String {
        return { name ->
            context.filesDir.resolve(name).absolutePath
        }
    }
}
