package app.shapeshifter.feature.onboarding.ui

import app.shapeshifter.core.base.inject.ActivityScope
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface OnboardingComponent {

    @IntoSet
    @Provides
    @ActivityScope
    fun bindProfilePresenterFactory(factory: OnboardingPresenterFactory): Presenter.Factory =
        factory

    @IntoSet
    @Provides
    @ActivityScope
    fun bindProfileUiFactory(factory: OnboardingUiFactory): Ui.Factory = factory
}
