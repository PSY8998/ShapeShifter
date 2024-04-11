package app.shapeshifter.inject

import android.app.Application
import app.core.base.inject.ApplicationScope
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@ApplicationScope
@Component
abstract class ApplicationComponent(
    @get:Provides val application: Application,
) {

    companion object
}
