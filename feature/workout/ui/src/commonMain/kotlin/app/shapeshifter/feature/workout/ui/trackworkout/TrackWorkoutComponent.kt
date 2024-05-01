package app.shapeshifter.feature.workout.ui.trackworkout

import app.shapeshifter.core.base.inject.ActivityScope
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface TrackWorkoutComponent {

    @IntoSet
    @ActivityScope
    @Provides
    fun provideUiFactory(factory: TrackWorkoutUiFactory): Ui.Factory = factory

    @IntoSet
    @ActivityScope
    @Provides
    fun providePresenterFactory(factory: TrackWorkoutPresenterFactory): Presenter.Factory = factory
}
