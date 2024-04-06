package app.feature.home.ui

import app.core.base.inject.ActivityScope
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface HomeComponent {

    @IntoSet
    @Provides
    @ActivityScope
    fun bindEpisodeDetailsPresenterFactory(factory: HomePresenterFactory): Presenter.Factory =
        factory

    @IntoSet
    @Provides
    @ActivityScope
    fun bindEpisodeDetailsUiFactoryFactory(factory: HomeUiFactory): Ui.Factory = factory
}