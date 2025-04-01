package app.shapeshifter.feature.workout.ui.postworkout

import app.shapeshifter.core.base.inject.ActivityScope
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface PostWorkoutComponent {

    @IntoSet
    @ActivityScope
    @Provides
    fun provideUiFactory(factory: PostWorkoutUiFactory): Ui.Factory = factory

    @IntoSet
    @ActivityScope
    @Provides
    fun providePresenterFactory(factory: PostWorkoutPresenterFactory): Presenter.Factory = factory
}
