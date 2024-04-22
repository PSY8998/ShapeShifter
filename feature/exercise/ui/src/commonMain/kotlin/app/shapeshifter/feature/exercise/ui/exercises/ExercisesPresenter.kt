package app.shapeshifter.feature.exercise.ui.exercises

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.shapeshifter.common.ui.compose.screens.ExerciseDetailScreen
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import app.shapeshifter.feature.exercise.data.ExerciseRepository
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ExercisesPresenterFactory(
    private val presenterFactory: (Navigator) -> ExercisesPresenter ,
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
    private val exerciseRepository: ExerciseRepository,
) : Presenter<ExercisesState> {

    @Composable
    override fun present(): ExercisesState {
        fun eventSink(exerciseUiEvent: ExerciseUiEvent) {
            when (exerciseUiEvent) {
                is ExerciseUiEvent.OpenCreateExercise -> {
                    navigator.goTo(ExerciseDetailScreen)
                }
            }
        }

        val exercises by exerciseRepository.allExercises().collectAsState(initial = emptyList())

        return ExercisesState.Exercises(
            eventSink = ::eventSink,
            exercises = exercises
        )
    }

}
