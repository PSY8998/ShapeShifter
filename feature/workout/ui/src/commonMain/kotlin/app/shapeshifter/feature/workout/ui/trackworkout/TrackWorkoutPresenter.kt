package app.shapeshifter.feature.workout.ui.trackworkout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.shapeshifter.common.ui.compose.screens.CreateWorkoutPlanScreen
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import app.shapeshifter.common.ui.compose.screens.FinishWorkoutScreen
import app.shapeshifter.common.ui.compose.screens.ExerciseSequenceScreen
import app.shapeshifter.common.ui.compose.screens.TrackWorkoutScreen
import app.shapeshifter.data.models.plans.WorkoutPlan
import app.shapeshifter.data.models.plans.WorkoutPlanSession
import app.shapeshifter.data.models.workoutlog.ExerciseLog
import app.shapeshifter.data.models.workoutlog.WorkoutLog
import app.shapeshifter.data.models.workoutlog.WorkoutSession
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
import app.shapeshifter.feature.workout.domain.SelectWorkoutPlanUseCase
import app.shapeshifter.feature.workout.domain.UpdateRestTimeUseCase
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuitx.effects.LaunchedImpressionEffect
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import shapeshifter.feature.workout.ui.generated.resources.Res
import kotlinx.coroutines.flow.MutableStateFlow
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
) : Presenter<TrackWorkoutUiState> {


    @Composable
    override fun present(): TrackWorkoutUiState {
        val scope = rememberCoroutineScope()

        val workoutSessionFlow = MutableStateFlow<WorkoutSession?>(null)

        var workoutId: Long by rememberSaveable { mutableLongStateOf(0L) }
        val workoutSession by observeWorkoutDetailsUseCase.flow.collectAsRetainedState(null)

        LaunchedImpressionEffect(Unit) {
            if (workoutSessionFlow.value == null) {
                val insertedWorkoutId = createWorkoutUseCase(Unit)
                    .getOrNull()

                workoutId = insertedWorkoutId ?: 0
            }
        }

        LaunchedEffect(screen.workoutPlanId) {
            val session = getWorkoutSessionUseCase(
                GetWorkoutSessionUseCase.Params(screen.workoutPlanId),
            ).getOrNull()
            if (session != null) {
                workoutSessionFlow.value = session
                workoutId = session.workoutLog.id
            }
        }

        LaunchedEffect(workoutId) {
            observeWorkoutDetailsUseCase(
                ObserveWorkoutDetailsUseCase.Params(
                    workoutLogId = workoutId,
                    workoutPlanId = screen.workoutPlanId,
                ),
            )
        }

        val selectExercisesNavigator =
            rememberAnsweringNavigator<ExercisesScreen.Result>(navigator) { result ->
                when (result) {
                    is ExercisesScreen.Result.ReplaceExercise -> {
                        val exerciseLogId = result.exerciseLogId
                        val selectedExerciseId = result.exerciseId
                        scope.launch {
                            val deletedLog: ExerciseLog? = removeExerciseLogUseCase.invoke(
                                RemoveExerciseLogUseCase.Params(
                                    exerciseLogId,
                                ),
                            ).getOrNull()

                            addExerciseUseCase(
                                AddExerciseLogUseCase.Params(
                                    workoutLogId = workoutId,
                                    exerciseIds = listOf(selectedExerciseId),
                                    workoutPlanId = WorkoutPlan.QuickWorkoutId,
                                    index = deletedLog?.index ?: 0,
                                ),
                            )
                        }
                    }

                    is ExercisesScreen.Result.SelectedExercises -> {
                        val selectedExerciseIds = result.exerciseIds
                        scope.launch {
                            if (workoutId != 0L) {
                                addExerciseUseCase(
                                    AddExerciseLogUseCase.Params(
                                        workoutLogId = workoutId,
                                        exerciseIds = selectedExerciseIds,
                                        workoutPlanId = WorkoutPlan.QuickWorkoutId,
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
                                workoutLog = workoutSession?.workoutLog ?: return@launch,
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
                                workoutLog = session.workoutLog.copy(
                                    restFinishTimeInMillis = System.currentTimeMillis() + event.exerciseLog.restTimeDuration,
                                ),
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

                is TrackWorkoutUiEvent.OnFinishWorkout -> {
                    scope.launch {
                        navigator.goTo(FinishWorkoutScreen(event.workoutSession.workoutLog.id))
//                        val result = finishWorkoutUseCase(
//                            params = FinishWorkoutUseCase.Params(
//                                workoutSession = event.workoutSession,
//                            ),
//                        )
                    }
                }

                is TrackWorkoutUiEvent.OnCompleteRestTime -> {

                }

                is TrackWorkoutUiEvent.OnReorderExercises -> {
                    scope.launch {
                        navigator.goTo(ExerciseSequenceScreen(workoutId))
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
                restTimeDurationInSecs = workoutSession?.workoutLog?.restFinishTimeInMillis?.let {
                    it - System.currentTimeMillis()
                }?.takeIf { it > 0 } ?: 0,
                eventSink = ::eventSink,
            )
        }
    }
}
