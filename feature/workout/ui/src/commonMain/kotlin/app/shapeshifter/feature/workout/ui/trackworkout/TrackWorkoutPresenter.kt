package app.shapeshifter.feature.workout.ui.trackworkout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.shapeshifter.common.ui.compose.screens.ExerciseSequenceScreen
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import app.shapeshifter.common.ui.compose.screens.PostWorkoutScreen
import app.shapeshifter.common.ui.compose.screens.TrackWorkoutScreen
import app.shapeshifter.feature.workout.domain.AddExerciseLogUseCase
import app.shapeshifter.feature.workout.domain.CreateSetUseCase
import app.shapeshifter.feature.workout.domain.CreateWorkoutUseCase
import app.shapeshifter.feature.workout.domain.DeleteSetUseCase
import app.shapeshifter.feature.workout.domain.DiscardWorkoutUseCase
import app.shapeshifter.feature.workout.domain.FinishWorkoutUseCase
import app.shapeshifter.feature.workout.domain.FinishedSetUseCase
import app.shapeshifter.feature.workout.domain.GetWorkoutSessionUseCase
import app.shapeshifter.feature.workout.domain.ObserveWorkoutDetailsUseCase
import app.shapeshifter.feature.workout.domain.RemoveExerciseLogUseCase
import app.shapeshifter.feature.workout.domain.UpdateRestTimeUseCase
import app.shapeshifter.feature.workout.domain.UpdateSetLogUseCase
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuitx.effects.LaunchedImpressionEffect
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlin.time.Instant
import kotlinx.coroutines.launch

@Inject
class TrackWorkoutPresenterFactory(
    private val presenterFactory: (Navigator, TrackWorkoutScreen) -> TrackWorkoutPresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when (screen) {
            is TrackWorkoutScreen -> presenterFactory(navigator, screen)
            else -> null
        }
    }
}

@Inject
class TrackWorkoutPresenter(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: TrackWorkoutScreen,
    private val addExerciseUseCase: AddExerciseLogUseCase,
    private val observeWorkoutDetailsUseCase: ObserveWorkoutDetailsUseCase,
    private val createWorkoutUseCase: CreateWorkoutUseCase,
    private val discardWorkoutUseCase: DiscardWorkoutUseCase,
    private val createSetUseCase: CreateSetUseCase,
    private val finishedSetUseCase: FinishedSetUseCase,
    private val deleteSetUseCase: DeleteSetUseCase,
    private val finishWorkoutUseCase: FinishWorkoutUseCase,
    private val removeExerciseLogUseCase: RemoveExerciseLogUseCase,
    private val updateRestTimeUseCase: UpdateRestTimeUseCase,
    private val getWorkoutSessionUseCase: GetWorkoutSessionUseCase,
    private val updateSetLogUseCase: UpdateSetLogUseCase,
) : Presenter<TrackWorkoutUiState> {

    @Composable
    override fun present(): TrackWorkoutUiState {
        val scope = rememberCoroutineScope()

        var workoutLogId by rememberSaveable { mutableLongStateOf(screen.workoutLogId) }

        val workoutSession by observeWorkoutDetailsUseCase.flow.collectAsState(null)

        if (workoutLogId != 0L) {
            LaunchedEffect(workoutLogId) {
                observeWorkoutDetailsUseCase(
                    ObserveWorkoutDetailsUseCase.Params(
                        workoutLogId = workoutLogId,
                    ),
                )
            }
        }

        LaunchedImpressionEffect(screen.workoutPlanId, screen.workoutLogId) {
            val insertedWorkoutId = if (screen.workoutPlanId == null) {
                // Create an empty workout session
                createWorkoutUseCase(CreateWorkoutUseCase.Params(workoutPlanId = null))
                    .getOrNull()
            } else {
                // Create a workout session based on an existing workout plan session
                createWorkoutUseCase(CreateWorkoutUseCase.Params(workoutPlanId = screen.workoutPlanId))
                    .getOrNull()
            } ?: 0L

            workoutLogId = insertedWorkoutId
        }

        val selectExercisesNavigator =
            rememberAnsweringNavigator<ExercisesScreen.Result>(navigator) { result ->
                when (result) {
                    is ExercisesScreen.Result.ReplaceExercise -> {
                        val exerciseLogId = result.exerciseLogId
                        val selectedExerciseId = result.exerciseId
                        scope.launch {
                            val exerciseLogToReplace =
                                workoutSession?.exerciseSessions?.find { it.exercise.id == exerciseLogId }

                            removeExerciseLogUseCase.invoke(
                                RemoveExerciseLogUseCase.Params(
                                    exerciseLogId,
                                ),
                            ).getOrNull()

                            addExerciseUseCase(
                                AddExerciseLogUseCase.Params(
                                    workoutLogId = workoutLogId,
                                    exerciseIds = listOf(selectedExerciseId),
                                    workoutPlanId = null,
                                    index = exerciseLogToReplace?.exercise?.index?.toLong() ?: 0,
                                ),
                            )
                        }
                    }

                    is ExercisesScreen.Result.SelectedExercises -> {
                        val selectedExerciseIds = result.exerciseIds
                        scope.launch {
                            if (workoutLogId != 0L) {
                                addExerciseUseCase(
                                    AddExerciseLogUseCase.Params(
                                        workoutLogId = workoutLogId,
                                        exerciseIds = selectedExerciseIds,
                                        workoutPlanId = null,
                                        index = 0,
                                    ),
                                )
                            }
                        }
                    }
                }
            }

        fun eventSink(event: TrackWorkoutUiEvent) {
            when (event) {
                is TrackWorkoutUiEvent.GoBack -> navigator.pop()

                is TrackWorkoutUiEvent.OnAddExercise -> {
                    selectExercisesNavigator.goTo(ExercisesScreen(ExercisesScreen.Intent.SelectExercises))
                }

                is TrackWorkoutUiEvent.OnAddSet -> {
                    scope.launch {
                        createSetUseCase(
                            params = CreateSetUseCase.Params(
                                exerciseLogId = event.exerciseLogId,
                                exerciseId = event.exerciseId,
                                workoutPlanId = event.workoutPlanId,
                                workoutLogId = event.workoutLogId,
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
                        val session = workoutSession ?: return@launch
                        finishedSetUseCase(
                            params = FinishedSetUseCase.Params(
                                setLog = event.set,
                                workoutLog = session.workout,
                            ),
                        )
                    }
                }

                is TrackWorkoutUiEvent.OnDeleteSet -> {
                    scope.launch {
                        deleteSetUseCase(
                            params = DeleteSetUseCase.Params(
                                setLog = event.set,
                            ),
                        )
                    }
                }

                is TrackWorkoutUiEvent.OnFinishWorkoutWithDate -> {
                    scope.launch {
                        finishWorkoutUseCase(
                            params = FinishWorkoutUseCase.Params(
                                workoutSession = event.workoutSession.copy(
                                    workout = event.workoutSession.workout.copy(
                                        startTime = event.selectedDate.let {
                                            Instant.fromEpochMilliseconds(it)
                                        },
                                    ),
                                ),
                            ),
                        )
                        navigator.goTo(PostWorkoutScreen(workoutLogId))
                    }
                }

                is TrackWorkoutUiEvent.OnCompleteRestTime -> {

                }

                is TrackWorkoutUiEvent.OnReorderExercises -> {
                    scope.launch {
                        navigator.goTo(ExerciseSequenceScreen(workoutLogId))
                    }
                }

                is TrackWorkoutUiEvent.OnReplaceExercise -> {
                    scope.launch {
                        selectExercisesNavigator.goTo(
                            ExercisesScreen(
                                ExercisesScreen.Intent.ReplaceExercise(
                                    event.exerciseLog.id,
                                ),
                            ),
                        )
                    }
                }

                is TrackWorkoutUiEvent.OnRemoveExercise -> {
                    scope.launch {
                        removeExerciseLogUseCase(
                            params = RemoveExerciseLogUseCase.Params(
                                exerciseLogId = event.exerciseLog.id,
                            ),
                        )
                    }
                }

                is TrackWorkoutUiEvent.OnUpdateRestTime -> {
                    scope.launch {
                        updateRestTimeUseCase(
                            params = UpdateRestTimeUseCase.Params(
                                restTimeDurationInSecs = ((event.minutes * 60) + event.seconds).toLong(),
                                exerciseLog = event.exerciseLog,
                            ),
                        )
                    }
                }

                is TrackWorkoutUiEvent.OnUpdateSet -> {
                    scope.launch {
                        updateSetLogUseCase(
                            params = UpdateSetLogUseCase.Params(
                                setLog = event.setLog,
                            ),
                        )
                    }
                }
            }
        }

        return when {
            workoutSession == null -> TrackWorkoutUiState.Initial(
                eventSink = ::eventSink,
            )

            workoutSession?.exerciseSessions.isNullOrEmpty() -> TrackWorkoutUiState.Empty(
                eventSink = ::eventSink,
            )

            else -> TrackWorkoutUiState.Filled(
                workoutSession = workoutSession!!,
                restTimeDurationInSecs = 0,
                eventSink = ::eventSink,
            )
        }
    }
}
