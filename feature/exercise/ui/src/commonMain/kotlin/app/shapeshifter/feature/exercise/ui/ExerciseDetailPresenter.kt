package app.shapeshifter.feature.exercise.ui

import androidx.compose.runtime.Composable
import app.shapeshifter.common.ui.compose.screens.ExerciseDetailScreen
import app.shapeshifter.feature.exercise.data.exercise.ExerciseRepository
import app.shapeshifter.data.models.Exercise
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ExerciseDetailPresenterFactory(
    private val presenterFactory: (ExerciseDetailScreen, Navigator) -> ExerciseDetailPresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when (screen) {
            is ExerciseDetailScreen -> presenterFactory(screen, navigator)
            else -> null
        }
    }
}

@Inject
class ExerciseDetailPresenter(
    @Assisted private val exerciseDetailScreen: ExerciseDetailScreen,
    @Assisted private val navigator: Navigator,
    private val exerciseRepository: ExerciseRepository,
) : Presenter<ExerciseDetailState> {
    @Composable
    override fun present(): ExerciseDetailState {
        fun eventSink(event: ExerciseDetailUiEvent) {
            when (event) {
                is ExerciseDetailUiEvent.GoBack -> navigator.pop()
                is ExerciseDetailUiEvent.CreateExercise -> {
                    val exercise = Exercise(
                        name = event.exerciseName,
                        instructions = "",
                    )
                    //exerciseRepository.insert(exercise)
                    navigator.pop()
                }
            }
        }

        return ExerciseDetailState(
            eventSink = ::eventSink,
        )
    }
}
