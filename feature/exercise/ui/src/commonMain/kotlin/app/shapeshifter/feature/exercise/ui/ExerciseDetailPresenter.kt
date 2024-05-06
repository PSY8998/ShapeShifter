package app.shapeshifter.feature.exercise.ui

import androidx.compose.runtime.Composable
import app.shapeshifter.common.ui.compose.screens.ExerciseDetailScreen
import app.shapeshifter.data.models.Exercise
import app.shapeshifter.data.models.Muscles
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
) : Presenter<ExerciseDetailState> {
    @Composable
    override fun present(): ExerciseDetailState {
        fun eventSink(event: ExerciseDetailUiEvent) {
            when (event) {
                is ExerciseDetailUiEvent.GoBack -> navigator.pop()
                is ExerciseDetailUiEvent.CreateExercise -> {
                    val exercise = Exercise(
                        name = event.exerciseName,
                        primaryMuscle = Muscles.OTHER,
                        secondaryMuscle = emptyList(),
                        imageUrl = "",
                    )
                    // exerciseRepository.insert(exercise)
                    navigator.pop()
                }
            }
        }

        return ExerciseDetailState(
            eventSink = ::eventSink,
        )
    }
}
