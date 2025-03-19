package app.shapeshifter.feature.onboarding.ui

import androidx.compose.runtime.Composable
import app.shapeshifter.common.ui.compose.screens.HomeScreen
import app.shapeshifter.common.ui.compose.screens.OnboardingScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class OnboardingPresenterFactory(
    private val presenterFactory: (OnboardingScreen, Navigator) -> OnboardingPresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when (screen) {
            is OnboardingScreen -> presenterFactory(screen, navigator)
            else -> null
        }
    }

}

@Inject
class OnboardingPresenter(
    @Assisted private val screen: OnboardingScreen,
    @Assisted private val navigator: Navigator,
) : Presenter<OnboardingUiState> {
    @Composable
    override fun present(): OnboardingUiState {

        fun eventSink(event: OnboardingUiEvent) {
            when (event) {
                is OnboardingUiEvent.OnFinish -> navigator.goTo(HomeScreen)
            }
        }

        return OnboardingUiState(
            eventSink = ::eventSink,
        )
    }
}
