package app.shapeshifter.inject

import android.app.Activity
import app.shapeshifter.core.base.inject.ActivityScope
import app.shapeshifter.feature.exercise.ui.ExerciseDetailComponent
import app.shapeshifter.feature.exercise.ui.exercises.ExercisesComponent
import app.shapeshifter.feature.home.ui.HomeComponent
import app.shapeshifter.feature.onboarding.ui.OnboardingComponent
import app.shapeshifter.feature.profile.ui.ProfileComponent
import app.shapeshifter.feature.root.ui.ShapeShifterContent
import app.shapeshifter.feature.workout.ui.createworkoutplan.CreateWorkoutPlanComponent
import app.shapeshifter.feature.workout.ui.exercisesequence.ExerciseSequenceComponent
import app.shapeshifter.feature.workout.ui.finishworkout.FinishWorkoutComponent
import app.shapeshifter.feature.workout.ui.savedworkouts.SavedWorkoutsComponent
import app.shapeshifter.feature.workout.ui.trackworkout.TrackWorkoutComponent
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
    ExerciseDetailComponent,
    ExercisesComponent,
    SavedWorkoutsComponent,
    TrackWorkoutComponent,
    CreateWorkoutPlanComponent,
    ProfileComponent,
    FinishWorkoutComponent,
    ExerciseSequenceComponent,
    OnboardingComponent {
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
