package app.shapeshifter.feature.profile.ui

import androidx.compose.runtime.Composable
import app.shapeshifter.common.ui.compose.screens.ProfileScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

object EmptyUiState : CircuitUiState

@Inject
class ProfilePresenterFactory(
    private val presenterFactory: (ProfileScreen, Navigator) -> ProfilePresenter
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when(screen){
            is ProfileScreen -> presenterFactory(screen, navigator)
            else -> null
        }
    }

}

@Inject
class ProfilePresenter(
    @Assisted private val screen: ProfileScreen,
    @Assisted private val navigator: Navigator,
) : Presenter<EmptyUiState> {
    @Composable
    override fun present(): EmptyUiState {
        return EmptyUiState
    }

}
