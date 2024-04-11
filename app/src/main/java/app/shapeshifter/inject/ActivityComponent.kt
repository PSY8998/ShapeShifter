package app.shapeshifter.inject

import android.app.Activity
import app.core.base.inject.ActivityScope
import app.feature.home.ui.HomeComponent
import app.feature.home.ui.ShapeShifterContent
import app.feature.home.ui.exercisedetail.ExerciseDetailComponent
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@ActivityScope
@Component
abstract class ActivityComponent(
    @get:Provides val activity: Activity,
    @Component val applicationComponent: ApplicationComponent,
) : HomeComponent,
    ExerciseDetailComponent {
    abstract val shapeShifterContent: ShapeShifterContent

    @ActivityScope
    @Provides
    fun provideCircuit(
        uiFactories: Set<Ui.Factory>,
        presenterFactories: Set<Presenter.Factory>,
    ) = Circuit.Builder()
        .addUiFactories(uiFactories)
        .addPresenterFactories(presenterFactories)
        .build()

    companion object
}
