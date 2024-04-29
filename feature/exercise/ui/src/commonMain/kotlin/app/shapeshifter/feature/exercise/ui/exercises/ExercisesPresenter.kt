package app.shapeshifter.feature.exercise.ui.exercises

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import app.shapeshifter.common.ui.compose.screens.ExerciseDetailScreen
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import app.shapeshifter.data.models.Exercise
import app.shapeshifter.feature.exercise.domain.FetchExercisesUseCase
import app.shapeshifter.feature.exercise.data.exercise.ExerciseRepository
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ExercisesPresenterFactory(
    private val presenterFactory: (Navigator) -> ExercisesPresenter,
) : Presenter.Factory {

    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when (screen) {
            is ExercisesScreen -> {
                presenterFactory(navigator)
            }

            else -> null
        }
    }
}

@Inject
class ExercisesPresenter(
    @Assisted private val navigator: Navigator,
    private val fetchExercisesUseCase: FetchExercisesUseCase,
    private val exerciseRepository: ExerciseRepository,
) : Presenter<ExercisesUiState> {

    @Composable
    override fun present(): ExercisesUiState {
        LaunchedEffect(Unit) {
            val result = fetchExercisesUseCase(Unit)
            result.isFailure
        }

        fun eventSink(exerciseUiEvent: ExerciseUiEvent) {
            when (exerciseUiEvent) {
                is ExerciseUiEvent.OpenCreateExercise -> {
                    navigator.goTo(ExerciseDetailScreen)
                }
            }
        }


        val exercises: List<Exercise> by exerciseRepository.observeExercises()
            .collectAsRetainedState(initial = emptyList())

        return if (exercises.isEmpty()) {
            ExercisesUiState.Empty(
                eventSink = ::eventSink,
            )
        } else {
            ExercisesUiState.Exercises(
                eventSink = ::eventSink,
                exercises = exercises,
            )
        }
    }
}
