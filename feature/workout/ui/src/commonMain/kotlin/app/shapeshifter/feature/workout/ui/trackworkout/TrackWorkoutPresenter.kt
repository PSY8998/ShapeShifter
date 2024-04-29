package app.shapeshifter.feature.workout.ui.trackworkout

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import app.shapeshifter.common.ui.compose.screens.TrackWorkoutScreen
import app.shapeshifter.feature.workout.ui.savedworkouts.SavedWorkoutsPresenter
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Inject

@Inject
class TrackWorkoutPresenterFactory(
    private val provideFactory: () -> TrackWorkoutPresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when (screen) {
            is TrackWorkoutScreen -> provideFactory()
            else -> null
        }
    }
}

@Inject
class TrackWorkoutPresenter : Presenter<TrackWorkoutUiState> {

    @Composable
    override fun present(): TrackWorkoutUiState {
        return TrackWorkoutUiState
    }
}
