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
import app.shapeshifter.data.models.workout.WorkoutWithExercisesAndSets
import app.shapeshifter.feature.workout.domain.CreateWorkoutUseCase
import app.shapeshifter.feature.workout.domain.DiscardWorkoutUseCase
import app.shapeshifter.feature.workout.domain.ObserveWorkoutDetailsUseCase
import app.shapeshifter.feature.workout.domain.StoreWorkoutExerciseUseCase
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
    private val storeWorkoutExerciseUseCase: StoreWorkoutExerciseUseCase,
    private val observeWorkoutDetailsUseCase: ObserveWorkoutDetailsUseCase,
    private val createWorkoutUseCase: CreateWorkoutUseCase,
    private val discardWorkoutUseCase: DiscardWorkoutUseCase,
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
                            StoreWorkoutExerciseUseCase.Params(
                                workoutId = workoutId,
                                exerciseIds = selectedExerciseIds,
                            ),
                        )
                    }
                }
            }

        val workoutDetail: WorkoutWithExercisesAndSets?
            by observeWorkoutDetailsUseCase.flow.collectAsRetainedState(null)

        fun eventSink(event: TrackWorkoutUiEvent) {
            when (event) {
                is TrackWorkoutUiEvent.GoBack -> navigator.pop()

                is TrackWorkoutUiEvent.OnAddExercise -> {
                    answeringNavigator.goTo(ExercisesScreen(true))
                }

                is TrackWorkoutUiEvent.DiscardWorkout -> {
                    scope.launch {
                        val result = discardWorkoutUseCase(
                            params = DiscardWorkoutUseCase.Params(
                                workout = workoutDetail?.workout ?: return@launch,
                            ),
                        )

                        result
                        navigator.pop()
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
            workoutDetail = workoutDetail,
            eventSink = ::eventSink,
        )
    }
}
