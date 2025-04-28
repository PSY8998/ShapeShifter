package app.shapeshifter.feature.onboarding.ui

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class OnboardingUiState(
    val currentPageIndex: Int,
    val selectedGoal: String = "",
    val isNextEnabled: Boolean = true,
    val eventSink: (OnboardingUiEvent) -> Unit,
) : CircuitUiState

sealed interface OnboardingUiEvent : CircuitUiEvent {
    data object GoNext : OnboardingUiEvent
    data object GoPrevious : OnboardingUiEvent
    data class SelectGoal(val goal: String) : OnboardingUiEvent
    data object Finish : OnboardingUiEvent
}
