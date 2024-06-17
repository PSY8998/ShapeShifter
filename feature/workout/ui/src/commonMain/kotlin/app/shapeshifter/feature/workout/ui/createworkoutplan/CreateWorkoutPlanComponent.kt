package app.shapeshifter.feature.workout.ui.createworkoutplan

import app.shapeshifter.core.base.inject.ActivityScope
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface CreateWorkoutPlanComponent {

    @IntoSet
    @ActivityScope
    @Provides
    fun provideUiFactory(factory: CreateWorkoutPlanUiFactory): Ui.Factory = factory

    @IntoSet
    @ActivityScope
    @Provides
    fun providePresenterFactory(factory: CreateWorkoutPlanPresenterFactory): Presenter.Factory = factory
}
