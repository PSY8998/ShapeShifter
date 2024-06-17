package app.shapeshifter.feature.workout.ui.savedworkouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import app.shapeshifter.common.ui.compose.screens.CreateWorkoutPlanScreen
import app.shapeshifter.common.ui.compose.screens.SavedWorkoutsScreen
import app.shapeshifter.common.ui.compose.screens.TrackWorkoutScreen
import app.shapeshifter.feature.workout.domain.DiscardWorkoutUseCase
import app.shapeshifter.feature.workout.domain.ObserveActiveWorkoutUseCase
import app.shapeshifter.feature.workout.domain.StartWorkoutUseCase
import com.slack.circuit.retained.collectAsRetainedState
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
    private val observeActiveWorkoutUseCase: ObserveActiveWorkoutUseCase,
    private val discardWorkoutUseCase: DiscardWorkoutUseCase,
) : Presenter<SavedWorkoutsUiState> {

    @Composable
    override fun present(): SavedWorkoutsUiState {
        val scope = rememberCoroutineScope()
        val activeWorkout by observeActiveWorkoutUseCase.flow.collectAsRetainedState(null)

        fun eventSink(event: SavedWorkoutsUiEvent) {
            when (event) {
                is SavedWorkoutsUiEvent.OpenQuickWorkout -> {
                    navigator.goTo(TrackWorkoutScreen)
                }

                is SavedWorkoutsUiEvent.DiscardWorkout -> {
                    scope.launch {
                        discardWorkoutUseCase(
                            DiscardWorkoutUseCase.Params(event.workoutLog),
                        )
                    }
                }

                is SavedWorkoutsUiEvent.DiscardAndStartNewWorkout -> {
                    scope.launch {
                        discardWorkoutUseCase(
                            DiscardWorkoutUseCase.Params(event.workoutLog),
                        )
                        navigator.goTo(TrackWorkoutScreen)
                    }
                }

                is SavedWorkoutsUiEvent.CreateWorkoutPlan -> {
                    navigator.goTo(CreateWorkoutPlanScreen(event.planName))
                }
            }
        }

        LaunchedEffect(Unit) {
            observeActiveWorkoutUseCase(Unit)
        }

        return SavedWorkoutsUiState(
            activeWorkout = activeWorkout,
            eventSink = ::eventSink,
        )
    }
}
