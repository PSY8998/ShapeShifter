package app.shapeshifter.feature.workout.ui.savedworkouts

import app.shapeshifter.core.base.inject.ActivityScope
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface SavedWorkoutsComponent {

    @IntoSet
    @ActivityScope
    @Provides
    fun provideUiFactory(factory: SavedWorkoutsUiFactory): Ui.Factory = factory

    @IntoSet
    @ActivityScope
    @Provides
    fun providePresenterFactory(factory: SavedWorkoutsPresenterFactory): Presenter.Factory = factory
}
