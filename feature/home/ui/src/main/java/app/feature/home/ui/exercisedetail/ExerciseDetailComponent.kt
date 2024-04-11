package app.feature.home.ui.exercisedetail

import app.core.base.inject.ActivityScope
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface ExerciseDetailComponent {

    @IntoSet
    @Provides
    @ActivityScope
    fun bindExerciseDetailPresenterFactory(
        factory: ExerciseDetailPresenterFactory,
    ): Presenter.Factory = factory

    @IntoSet
    @Provides
    @ActivityScope
    fun bindExerciseDetailUiFactory(
        factory: ExerciseDetailUiFactory,
    ): Ui.Factory = factory
}
