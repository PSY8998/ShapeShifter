package app.shapeshifter.feature.workout.ui.trackworkout

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import app.shapeshifter.common.ui.compose.screens.TrackWorkoutScreen
import app.shapeshifter.feature.workout.ui.savedworkouts.SavedWorkoutsPresenter
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class TrackWorkoutPresenterFactory(
    private val provideFactory: (Navigator) -> TrackWorkoutPresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when (screen) {
            is TrackWorkoutScreen -> provideFactory(navigator)
            else -> null
        }
    }
}

@Inject
class TrackWorkoutPresenter(
    @Assisted private val navigator: Navigator
) : Presenter<TrackWorkoutUiState> {

    @Composable
    override fun present(): TrackWorkoutUiState {
        fun eventSink(event: TrackWorkoutUiEvent){
            when(event){
                is TrackWorkoutUiEvent.GoBack -> navigator.pop()
                is TrackWorkoutUiEvent.OnAddExercise -> navigator.goTo(ExercisesScreen(true))
            }
        }
        return TrackWorkoutUiState(
            eventSink = :: eventSink
        )
    }
}
