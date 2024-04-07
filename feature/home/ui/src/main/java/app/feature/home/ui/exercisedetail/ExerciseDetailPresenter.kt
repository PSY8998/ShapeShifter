package app.feature.home.ui.exercisedetail

import androidx.compose.runtime.Composable
import app.feature.home.ui.EmptyUiState
import app.feature.home.ui.HomePresenter
import app.feature.home.ui.HomeScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ExerciseDetailPresenterFactory(
    private val presenterFactory: (ExerciseDetailScreen) -> ExerciseDetailPresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext
    ): Presenter<*>? {
        return when (screen) {
            is ExerciseDetailScreen -> presenterFactory(screen)
            else -> null
        }
    }

}

@Inject
class ExerciseDetailPresenter(
    @Assisted private val exerciseDetailScreen: ExerciseDetailScreen
) : Presenter<EmptyUiState> {
    @Composable
    override fun present(): EmptyUiState {
        return EmptyUiState()
    }
}