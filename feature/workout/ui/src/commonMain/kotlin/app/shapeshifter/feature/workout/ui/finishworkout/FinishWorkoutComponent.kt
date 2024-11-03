package app.shapeshifter.feature.workout.ui.finishworkout

import app.shapeshifter.core.base.inject.ActivityScope
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface FinishWorkoutComponent {

    @IntoSet
    @ActivityScope
    @Provides
    fun provideUiFactory(factory: FinishWorkoutUiFactory): Ui.Factory = factory

    @IntoSet
    @ActivityScope
    @Provides
    fun providePresenterFactory(factory: FinishWorkoutPresenterFactory): Presenter.Factory = factory
}
