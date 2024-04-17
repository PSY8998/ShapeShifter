package app.shapeshifter.feature.exercise.ui.exercises

import app.shapeshifter.core.base.inject.ActivityScope
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface ExercisesComponent {

    @IntoSet
    @Provides
    @ActivityScope
    fun bindExercisesPresenterFactory(
        factory: ExercisesPresenterFactory,
    ): Presenter.Factory = factory

    @IntoSet
    @Provides
    @ActivityScope
    fun bindExercisesUiFactory(
        factory: ExercisesUiFactory,
    ): Ui.Factory = factory
}
