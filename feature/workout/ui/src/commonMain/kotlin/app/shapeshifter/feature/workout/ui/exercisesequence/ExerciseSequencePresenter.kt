package app.shapeshifter.feature.workout.ui.exercisesequence

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import app.shapeshifter.common.ui.compose.screens.ExerciseSequenceScreen
import app.shapeshifter.data.models.workout.ExerciseLog
import app.shapeshifter.data.models.workout.WorkoutLogSession
import app.shapeshifter.data.models.workout.WorkoutSession
import app.shapeshifter.feature.workout.domain.ObserveWorkoutDetailsUseCase
import app.shapeshifter.feature.workout.domain.UpdateExerciseLogIndexUseCase
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.launch

@Inject
class ExerciseSequencePresenterFactory(
    private val provideFactory: (Navigator, ExerciseSequenceScreen) -> ExerciseSequencePresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when (screen) {
            is ExerciseSequenceScreen -> provideFactory(navigator, screen)
            else -> null
        }
    }

}

@Inject
class ExerciseSequencePresenter(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: ExerciseSequenceScreen,
    private val observeWorkoutDetailsUseCase: ObserveWorkoutDetailsUseCase,
    private val updateExerciseLogIndexUseCase: UpdateExerciseLogIndexUseCase,
) : Presenter<ExerciseSequenceUiState> {
    @Composable
    override fun present(): ExerciseSequenceUiState {

        val scope = rememberCoroutineScope()

        val workoutId: Long by rememberSaveable { mutableLongStateOf(screen.workoutLogId) }

        val workoutSession: WorkoutLogSession?
            by observeWorkoutDetailsUseCase.flow.collectAsRetainedState(null)

        if (workoutId != 0L) {
            LaunchedEffect(workoutId) {
                observeWorkoutDetailsUseCase(
                    params = ObserveWorkoutDetailsUseCase.Params(
                        workoutLogId = screen.workoutLogId,
                    ),
                )
            }
        }

        fun eventSink(event: ExerciseSequenceUiEvent) {
            when (event) {
                is ExerciseSequenceUiEvent.OnReorderedExercises -> {
                    scope.launch {
                        val exerciseLogs: List<ExerciseLog> =
                            event.exerciseSessions.map { it.exercise }
                        updateExerciseLogIndexUseCase(
                            params = UpdateExerciseLogIndexUseCase.Params(
                                exerciseLogs = exerciseLogs,
                            ),
                        )
                        navigator.pop()
                    }
                }
            }
        }

        val session = workoutSession
        return when {
            session == null || session.exerciseSessions.isEmpty() -> {
                ExerciseSequenceUiState.Empty(
                    eventSink = ::eventSink,
                )
            }

            else -> {
                ExerciseSequenceUiState.Filled(
                    eventSink = ::eventSink,
                    exerciseSessions = session.exerciseSessions,
                )
            }
        }
    }
}

