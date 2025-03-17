package app.shapeshifter.feature.onboarding.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.shapeshifter.common.ui.compose.screens.OnboardingScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject

@Inject
class OnboardingUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is OnboardingScreen -> {
                ui<OnboardingUiState> { state, modifier ->
                    Onboarding(state, modifier)
                }
            }

            else -> null
        }
    }

}

@Composable
fun Onboarding(
    state: OnboardingUiState,
    modifier: Modifier = Modifier,
){
    Text("Welcome")
}
