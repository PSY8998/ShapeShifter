package app.shapeshifter.feature.workout.ui.exercisesequence

import app.shapeshifter.core.base.inject.ActivityScope
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface ExerciseSequenceComponent {

    @IntoSet
    @ActivityScope
    @Provides
    fun provideUiFactory(factory: ExerciseSequenceUiFactory): Ui.Factory = factory

    @IntoSet
    @ActivityScope
    @Provides
    fun providePresenterFactory(factory: ExerciseSequencePresenterFactory): Presenter.Factory =
        factory
}
