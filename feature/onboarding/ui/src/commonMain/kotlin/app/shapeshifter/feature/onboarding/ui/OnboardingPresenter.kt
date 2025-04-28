package app.shapeshifter.feature.onboarding.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import app.shapeshifter.common.ui.compose.screens.HomeScreen
import app.shapeshifter.common.ui.compose.screens.OnboardingScreen
import app.shapeshifter.feature.onboarding.data.OnboardingPreferences
import app.shapeshifter.feature.onboarding.data.onboardingPages
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.launch

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
    private val onboardingPreferences: OnboardingPreferences,
) : Presenter<OnboardingUiState> {
    @Composable
    override fun present(): OnboardingUiState {
        var currentPageIndex by remember { mutableIntStateOf(0) }
        var selectedGoal by remember { mutableStateOf("") }

        val scope = rememberCoroutineScope()
        val pageCount = onboardingPages.size

        // Determine if next should be enabled based on current page and goal selection
        val isNextEnabled by remember(currentPageIndex, selectedGoal) {
            mutableStateOf(
                when (currentPageIndex) {
                    1 -> selectedGoal.isNotBlank() // Goal selection page
                    else -> true // Always enabled for other pages
                },
            )
        }

        fun eventSink(event: OnboardingUiEvent) {
            when (event) {
                is OnboardingUiEvent.GoNext -> {
                    if (currentPageIndex < pageCount - 1) {
                        currentPageIndex++
                    } else {
                        // Already on the last page, treat as Finish
                        eventSink(OnboardingUiEvent.Finish)
                    }
                }

                is OnboardingUiEvent.GoPrevious -> {
                    if (currentPageIndex > 0) {
                        currentPageIndex--
                    }
                }

                is OnboardingUiEvent.SelectGoal -> {
                    selectedGoal = event.goal
                }

                is OnboardingUiEvent.Finish -> scope.launch {
                    // Save the selected goal if needed
                    // Example: onboardingPreferences.saveUserGoal(selectedGoal)
                    onboardingPreferences.setCompleted(true)
                    navigator.resetRoot(HomeScreen) // Navigate to HomeScreen after finish
                }
            }
        }

        return OnboardingUiState(
            currentPageIndex = currentPageIndex,
            selectedGoal = selectedGoal,
            isNextEnabled = isNextEnabled,
            eventSink = ::eventSink,
        )
    }
}
