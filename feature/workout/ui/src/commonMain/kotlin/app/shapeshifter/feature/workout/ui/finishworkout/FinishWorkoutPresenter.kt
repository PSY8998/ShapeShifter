package app.shapeshifter.feature.workout.ui.finishworkout

import androidx.compose.runtime.Composable
import app.shapeshifter.common.ui.compose.screens.FinishWorkoutScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class FinishWorkoutPresenterFactory(
    private val presenterFactory: (Navigator) -> FinishWorkoutPresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when (screen) {
            is FinishWorkoutScreen -> presenterFactory(navigator)
            else -> null
        }
    }
}

@Inject
class FinishWorkoutPresenter(
    @Assisted private val navigator: Navigator,
) : Presenter<FinishWorkoutUiState> {
    @Composable
    override fun present(): FinishWorkoutUiState {
        return FinishWorkoutUiState
    }

}
