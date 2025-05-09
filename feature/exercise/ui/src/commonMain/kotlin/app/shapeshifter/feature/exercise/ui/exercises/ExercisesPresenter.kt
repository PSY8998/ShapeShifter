package app.shapeshifter.feature.exercise.ui.exercises

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import app.shapeshifter.common.ui.compose.screens.ExerciseDetailScreen
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import app.shapeshifter.data.models.ExerciseTemplate
import app.shapeshifter.feature.exercise.domain.FetchExercisesUseCase
import app.shapeshifter.feature.exercise.domain.ObserveExercisesUseCase
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.launch

@Inject
class ExercisesPresenterFactory(
    private val presenterFactory: (ExercisesScreen, Navigator) -> ExercisesPresenter,
) : Presenter.Factory {

    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when (screen) {
            is ExercisesScreen -> {
                presenterFactory(screen, navigator)
            }

            else -> null
        }
    }
}

@Inject
class ExercisesPresenter(
    @Assisted private val screen: ExercisesScreen,
    @Assisted private val navigator: Navigator,
    private val fetchExercisesUseCase: FetchExercisesUseCase,
    private val observeExercisesUseCase: ObserveExercisesUseCase,
) : Presenter<ExercisesUiState> {

    @Composable
    override fun present(): ExercisesUiState {

        LaunchedEffect(Unit) {
            launch {
                fetchExercisesUseCase(Unit)
            }

            launch {
                observeExercisesUseCase(Unit)
            }
        }

        fun eventSink(event: ExerciseUiEvent) {
            when (event) {
                is ExerciseUiEvent.OpenCreateExercise -> {
                    navigator.goTo(ExerciseDetailScreen)
                }

                is ExerciseUiEvent.SelectExercises -> {
                    navigator.pop(
                        result = ExercisesScreen.Result.SelectedExercises(
                            exerciseIds = event.ids,
                        ),
                    )
                }

                is ExerciseUiEvent.ReplaceExercise -> {
                    navigator.pop(
                        result = ExercisesScreen.Result.ReplaceExercise(
                            exerciseLogId = event.exerciseLogId,
                            exerciseId = event.exerciseId,
                        ),
                    )
                }
            }
        }

        val exercises: List<ExerciseTemplate> by observeExercisesUseCase.flow
            .collectAsRetainedState(initial = emptyList())

        return if (exercises.isEmpty()) {
            ExercisesUiState.Empty(
                eventSink = ::eventSink,
            )
        } else {
            ExercisesUiState.Exercises(
                eventSink = ::eventSink,
                exercises = exercises,
                intent = screen.intent,
            )
        }
    }
}
