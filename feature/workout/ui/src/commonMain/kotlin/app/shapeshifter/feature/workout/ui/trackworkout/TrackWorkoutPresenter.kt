package app.shapeshifter.feature.workout.ui.trackworkout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import app.shapeshifter.common.ui.compose.screens.TrackWorkoutScreen
import app.shapeshifter.data.models.workoutlog.WorkoutSession
import app.shapeshifter.feature.workout.domain.CreateSetUseCase
import app.shapeshifter.feature.workout.domain.CreateWorkoutUseCase
import app.shapeshifter.feature.workout.domain.DeleteSetUseCase
import app.shapeshifter.feature.workout.domain.DiscardWorkoutUseCase
import app.shapeshifter.feature.workout.domain.FinishWorkoutUseCase
import app.shapeshifter.feature.workout.domain.FinishedSetUseCase
import app.shapeshifter.feature.workout.domain.ObserveWorkoutDetailsUseCase
import app.shapeshifter.feature.workout.domain.AddExerciseLogUseCase
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuitx.effects.LaunchedImpressionEffect
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.launch

@Inject
class TrackWorkoutPresenterFactory(
    private val presenterFactory: (Navigator) -> TrackWorkoutPresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when (screen) {
            is TrackWorkoutScreen -> presenterFactory(navigator)
            else -> null
        }
    }
}

@Inject
class TrackWorkoutPresenter(
    @Assisted private val navigator: Navigator,
    private val storeWorkoutExerciseUseCase: AddExerciseLogUseCase,
    private val observeWorkoutDetailsUseCase: ObserveWorkoutDetailsUseCase,
    private val createWorkoutUseCase: CreateWorkoutUseCase,
    private val discardWorkoutUseCase: DiscardWorkoutUseCase,
    private val createSetUseCase: CreateSetUseCase,
    private val finishedSetUseCase: FinishedSetUseCase,
    private val deleteSetUseCase: DeleteSetUseCase,
    private val finishWorkoutUseCase: FinishWorkoutUseCase,
) : Presenter<TrackWorkoutUiState> {

    @Composable
    override fun present(): TrackWorkoutUiState {
        val scope = rememberCoroutineScope()

        var workoutId: Long by rememberSaveable { mutableLongStateOf(0L) }

        LaunchedImpressionEffect(Unit) {
            val insertedWorkoutId = createWorkoutUseCase(Unit)
                .getOrNull()

            workoutId = insertedWorkoutId ?: 0
        }

        val answeringNavigator =
            rememberAnsweringNavigator<ExercisesScreen.Result>(navigator) { result ->
                val selectedExerciseIds = result.exerciseIds
                scope.launch {
                    if (workoutId != 0L) {
                        storeWorkoutExerciseUseCase(
                            AddExerciseLogUseCase.Params(
                                workoutId = workoutId,
                                exerciseIds = selectedExerciseIds,
                            ),
                        )
                    }
                }
            }

        val workoutSession: WorkoutSession?
            by observeWorkoutDetailsUseCase.flow.collectAsRetainedState(null)

        fun eventSink(event: TrackWorkoutUiEvent) {
            when (event) {
                is TrackWorkoutUiEvent.GoBack -> navigator.pop()

                is TrackWorkoutUiEvent.OnAddExercise -> {
                    answeringNavigator.goTo(ExercisesScreen(true))
                }

                is TrackWorkoutUiEvent.OnAddSet -> {
                    scope.launch {
                        createSetUseCase(
                            params = CreateSetUseCase.Params(
                                exerciseLogId = event.exerciseLogId,
                            ),
                        )
                    }
                }

                is TrackWorkoutUiEvent.DiscardWorkout -> {
                    scope.launch {
                        discardWorkoutUseCase(
                            params = DiscardWorkoutUseCase.Params(
                                workoutLog = workoutSession?.workout ?: return@launch,
                            ),
                        )

                        navigator.pop()
                    }
                }

                is TrackWorkoutUiEvent.OnSetCompleted -> {
                    scope.launch {
                        finishedSetUseCase(
                            params = FinishedSetUseCase.Params(
                                setLog = event.set,
                            ),
                        )
                    }
                }

                is TrackWorkoutUiEvent.OnDeleteSet ->{
                    scope.launch {
                        deleteSetUseCase(
                            params = DeleteSetUseCase.Params(
                                setLog = event.set,
                            )
                        )
                    }
                }

                is TrackWorkoutUiEvent.OnFinishWorkout ->{
                    scope.launch {
                        val result = finishWorkoutUseCase(
                            params = FinishWorkoutUseCase.Params(
                                workoutLog = event.workout
                            )
                        )
                    }
                }

            }
        }

        LaunchedEffect(workoutId) {
            observeWorkoutDetailsUseCase.invoke(
                params = ObserveWorkoutDetailsUseCase.Params(
                    workoutId = workoutId,
                ),
            )
        }

        return TrackWorkoutUiState(
            workoutSession = workoutSession,
            eventSink = ::eventSink,
        )
    }
}
