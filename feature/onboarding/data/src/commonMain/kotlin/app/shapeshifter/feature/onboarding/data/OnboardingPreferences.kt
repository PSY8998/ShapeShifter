package app.shapeshifter.feature.onboarding.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import app.shapeshifter.core.base.inject.DataStorePath
import app.shapeshifter.data.datastore.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class OnboardingPreferences(
    @DataStorePath private val filePathProvider: (String) -> String,
) {
    private val dataStore by lazy {
        createDataStore {
            filePathProvider("shapeshifter.onboarding")
        }
    }

    companion object {
        val COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    val isCompleted: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[COMPLETED] ?: false }

    suspend fun setCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[COMPLETED] = completed
        }
    }
}
