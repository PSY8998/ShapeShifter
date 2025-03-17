package app.shapeshifter.feature.workout.ui.savedworkouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import app.shapeshifter.common.ui.compose.screens.CreateWorkoutPlanScreen
import app.shapeshifter.common.ui.compose.screens.HomeScreen
import app.shapeshifter.common.ui.compose.screens.SavedWorkoutsScreen
import app.shapeshifter.common.ui.compose.screens.TrackWorkoutScreen
import app.shapeshifter.feature.workout.domain.DeleteWorkoutPlanUseCase
import app.shapeshifter.feature.workout.domain.DiscardWorkoutUseCase
import app.shapeshifter.feature.workout.domain.FetchWorkoutPlanSessionsUseCase
import app.shapeshifter.feature.workout.domain.ObserveActiveWorkoutUseCase
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
    private val fetchWorkoutPlanSessionsUseCase: FetchWorkoutPlanSessionsUseCase,
    private val deleteWorkoutPlanUseCase: DeleteWorkoutPlanUseCase,
) : Presenter<SavedWorkoutsUiState> {

    @Composable
    override fun present(): SavedWorkoutsUiState {
        val scope = rememberCoroutineScope()
        val activeWorkout by observeActiveWorkoutUseCase.flow.collectAsRetainedState(null)
        val workoutPlanSessions by fetchWorkoutPlanSessionsUseCase.flow.collectAsState(emptyList())

        fun eventSink(event: SavedWorkoutsUiEvent) {
            when (event) {
                is SavedWorkoutsUiEvent.SwitchToHome -> {
                    navigator.resetRoot(
                        newRoot = HomeScreen,
                        saveState = true,
                        restoreState = true,
                    )
                }

                is SavedWorkoutsUiEvent.OpenQuickWorkout -> {
                    navigator.goTo(
                        TrackWorkoutScreen(
                            workoutPlanId = -1,
                            workoutLogId = 0,
                        ),
                    )
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
                    }
                }

                is SavedWorkoutsUiEvent.CreateWorkoutPlan -> {
                    navigator.goTo(
                        CreateWorkoutPlanScreen(
                            intent = CreateWorkoutPlanScreen.Intent.NewWorkoutPlan(
                                planName = event.planName,
                                routineId = event.routineId,
                            ),
                        ),
                    )
                }

                is SavedWorkoutsUiEvent.OnDeleteWorkoutPlan -> {
                    scope.launch {
                        deleteWorkoutPlanUseCase(
                            DeleteWorkoutPlanUseCase.Params(event.workoutPlanSession),
                        )
                    }
                }

                is SavedWorkoutsUiEvent.OnEditWorkoutPlan -> {
                    scope.launch {
                        navigator.goTo(
                            CreateWorkoutPlanScreen(
                                intent = CreateWorkoutPlanScreen.Intent.EditWorkoutPlan(
                                    event.workoutPlanSession.workoutPlan.id,
                                ),
                            ),
                        )
                    }
                }

                is SavedWorkoutsUiEvent.OnStartWorkoutPlan -> {
                    scope.launch {
                        navigator.goTo(
                            TrackWorkoutScreen(
                                workoutPlanId = event.workoutPlanSession.workoutPlan.id,
                                workoutLogId = 0,
                            ),
                        )
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            observeActiveWorkoutUseCase(Unit)
            fetchWorkoutPlanSessionsUseCase(Unit)
        }

        return SavedWorkoutsUiState(
            workoutPlans = workoutPlanSessions,
            activeWorkout = activeWorkout,
            eventSink = ::eventSink,
        )
    }
}
