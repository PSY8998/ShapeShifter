package app.shapeshifter.feature.profile.ui

import app.shapeshifter.core.base.inject.ActivityScope
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface ProfileComponent {

    @IntoSet
    @Provides
    @ActivityScope
    fun bindProfilePresenterFactory(factory: ProfilePresenterFactory): Presenter.Factory =
        factory

    @IntoSet
    @Provides
    @ActivityScope
    fun bindProfileUiFactory(factory: ProfileUiFactory): Ui.Factory = factory
}
