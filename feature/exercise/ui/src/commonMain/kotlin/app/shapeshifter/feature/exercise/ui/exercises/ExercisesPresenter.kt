package app.shapeshifter.feature.exercise.ui.exercises

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import app.shapeshifter.common.ui.compose.screens.ExerciseDetailScreen
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import app.shapeshifter.data.models.Exercise
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
        val canSelect: Boolean = rememberSaveable { screen.canSelect }

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
                    navigator.pop(result = ExercisesScreen.Result(event.ids))
                }
            }
        }

        val exercises: List<Exercise> by observeExercisesUseCase.flow
            .collectAsRetainedState(initial = emptyList())

        return if (exercises.isEmpty()) {
            ExercisesUiState.Empty(
                eventSink = ::eventSink,
            )
        } else {
            ExercisesUiState.Exercises(
                eventSink = ::eventSink,
                exercises = exercises,
                canSelect = canSelect,
            )
        }
    }
}
