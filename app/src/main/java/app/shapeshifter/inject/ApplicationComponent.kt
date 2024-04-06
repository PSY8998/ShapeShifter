package app.shapeshifter.inject

import android.app.Application
import app.core.base.inject.ApplicationScope
import app.feature.home.ui.HomeComponent
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@ApplicationScope
@Component
abstract class ApplicationComponent(
    @get:Provides val application: Application,
) {

    companion object
}