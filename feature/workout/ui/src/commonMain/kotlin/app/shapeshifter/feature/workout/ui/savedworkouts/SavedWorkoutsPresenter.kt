package app.shapeshifter.feature.workout.ui.savedworkouts

import androidx.compose.runtime.Composable
import app.shapeshifter.common.ui.compose.screens.SavedWorkoutsScreen
import app.shapeshifter.common.ui.compose.screens.TrackWorkoutScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class SavedWorkoutsPresenterFactory(
    private val provideFactory: (Navigator) -> SavedWorkoutsPresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when (screen) {
            is SavedWorkoutsScreen -> provideFactory(navigator)
            else -> null
        }
    }
}

@Inject
class SavedWorkoutsPresenter(
    @Assisted private val navigator: Navigator,
) : Presenter<SavedWorkoutsUiState> {

    @Composable
    override fun present(): SavedWorkoutsUiState {
        fun eventSink(savedWorkoutUiEvent: SavedWorkoutsUiEvent) {
            when (savedWorkoutUiEvent) {
                is SavedWorkoutsUiEvent.OpenQuickWorkout -> {
                    navigator.goTo(TrackWorkoutScreen)
                }
            }
        }

        return SavedWorkoutsUiState(eventSink = ::eventSink)
    }
}
