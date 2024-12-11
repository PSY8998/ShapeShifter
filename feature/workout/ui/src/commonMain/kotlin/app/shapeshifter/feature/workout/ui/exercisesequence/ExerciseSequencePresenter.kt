package app.shapeshifter.feature.workout.ui.exercisesequence

import androidx.compose.runtime.Composable
import app.shapeshifter.common.ui.compose.screens.ExerciseSequenceScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

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
) : Presenter<ExerciseSequenceUiState> {
    @Composable
    override fun present(): ExerciseSequenceUiState {

        fun eventSink(event: ExerciseSequenceUiEvent) {

        }

        return ExerciseSequenceUiState(
            eventSink = ::eventSink,
        )
    }
}
