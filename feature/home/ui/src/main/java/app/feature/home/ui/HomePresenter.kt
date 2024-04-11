package app.feature.home.ui

import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

class EmptyUiState : CircuitUiState

@Inject
class HomePresenterFactory(
    private val presenterFactory: (HomeScreen, Navigator) -> HomePresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when (screen) {
            is HomeScreen -> presenterFactory(screen, navigator)
            else -> null
        }
    }
}

@Inject
class HomePresenter(
    @Assisted private val screen: HomeScreen,
    @Assisted private val navigator: Navigator,
) : Presenter<EmptyUiState> {
    @Composable
    override fun present(): EmptyUiState {
        return EmptyUiState()
    }
}
