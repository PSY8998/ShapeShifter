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
        var age by remember { mutableStateOf("") }
        var weight by remember { mutableStateOf("") }
        var height by remember { mutableStateOf("") }

        val scope = rememberCoroutineScope()
        val pageCount = onboardingPages.size // Get page count from imported list

        // Determine if next should be enabled based on current page and input state
        val isNextEnabled by remember(currentPageIndex, age, weight, height) {
            mutableStateOf(
                when (currentPageIndex) {
                    1 -> age.isNotBlank() // Index 1 = Age page
                    2 -> weight.isNotBlank() // Index 2 = Weight page
                    3 -> height.isNotBlank() // Index 3 = Height page
                    else -> true // Always enabled for Welcome and Finish pages
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

                is OnboardingUiEvent.UpdateAge -> age = event.value
                is OnboardingUiEvent.UpdateWeight -> weight = event.value
                is OnboardingUiEvent.UpdateHeight -> height = event.value
                is OnboardingUiEvent.Finish -> scope.launch {
                    // TODO: Save age, weight, height if required by the app logic
                    // Example: onboardingPreferences.saveUserDetails(age, weight, height)
                    onboardingPreferences.setCompleted(true)
                    navigator.resetRoot(HomeScreen) // Navigate to HomeScreen after finish
                }
            }
        }

        return OnboardingUiState(
            currentPageIndex = currentPageIndex,
            age = age,
            weight = weight,
            height = height,
            isNextEnabled = isNextEnabled,
            eventSink = ::eventSink,
        )
    }
}
