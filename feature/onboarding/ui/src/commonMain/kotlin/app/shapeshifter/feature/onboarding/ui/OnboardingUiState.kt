package app.shapeshifter.feature.onboarding.ui

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class OnboardingUiState(
    val currentPageIndex: Int,
    val age: String = "",
    val weight: String = "",
    val height: String = "",
    val isNextEnabled: Boolean = true,
    val eventSink: (OnboardingUiEvent) -> Unit,
) : CircuitUiState

sealed interface OnboardingUiEvent : CircuitUiEvent {

    data object GoNext : OnboardingUiEvent
    data object GoPrevious : OnboardingUiEvent
    data class UpdateAge(val value: String) : OnboardingUiEvent
    data class UpdateWeight(val value: String) : OnboardingUiEvent
    data class UpdateHeight(val value: String) : OnboardingUiEvent
    data object Finish : OnboardingUiEvent // Renamed from OnFinish for consistency
}
