package app.shapeshifter.feature.workout.ui.createworkoutplan

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import app.shapeshifter.common.ui.compose.screens.CreateWorkoutPlanScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class CreateWorkoutPlanPresenterFactory(
    private val provideFactory: (Navigator, CreateWorkoutPlanScreen) -> CreateWorkoutPlanPresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when (screen) {
            is CreateWorkoutPlanScreen -> provideFactory(navigator, screen)
            else -> null
        }
    }
}

@Inject
class CreateWorkoutPlanPresenter(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: CreateWorkoutPlanScreen,
) : Presenter<CreateWorkoutPlanUiState> {

    @Composable
    override fun present(): CreateWorkoutPlanUiState {
        val planName = rememberSaveable { screen.planName }
        return CreateWorkoutPlanUiState(
            planName = planName,
        )
    }

}
