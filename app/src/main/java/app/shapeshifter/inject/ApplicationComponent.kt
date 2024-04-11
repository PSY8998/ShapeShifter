package app.shapeshifter.inject

import android.app.Application
import app.shapeshifter.core.base.inject.ApplicationScope
import app.shapeshifter.feature.home.ui.sqldelight.data.SqlDelightDatabaseComponent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@ApplicationScope
@Component
abstract class ApplicationComponent(
    @get:Provides val application: Application,
): SqlDelightDatabaseComponent {

    companion object
}
