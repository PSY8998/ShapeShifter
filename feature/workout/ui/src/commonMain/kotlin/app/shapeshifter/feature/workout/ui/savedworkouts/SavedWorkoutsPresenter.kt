package app.shapeshifter.feature.workout.ui.savedworkouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import app.shapeshifter.common.ui.compose.screens.SavedWorkoutsScreen
import app.shapeshifter.common.ui.compose.screens.TrackWorkoutScreen
import app.shapeshifter.feature.workout.domain.StartWorkoutUseCase
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.launch

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
    private val startWorkoutUseCase: StartWorkoutUseCase,
) : Presenter<SavedWorkoutsUiState> {

    @Composable
    override fun present(): SavedWorkoutsUiState {
        val scope = rememberCoroutineScope()

        fun eventSink(savedWorkoutUiEvent: SavedWorkoutsUiEvent) {
            when (savedWorkoutUiEvent) {
                is SavedWorkoutsUiEvent.OpenQuickWorkout -> {
                    scope.launch {
                        val result = startWorkoutUseCase(Unit)
                            .getOrNull()

                        if (result is StartWorkoutUseCase.StartWorkoutResult.Workout) {
                            navigator.goTo(TrackWorkoutScreen)
                        } else {

                        }
                    }
                }
            }
        }

        return SavedWorkoutsUiState(eventSink = ::eventSink)
    }
}
