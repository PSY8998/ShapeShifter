package app.shapeshifter.feature.onboarding.ui

import com.slack.circuit.runtime.CircuitUiState

data class OnboardingUiState(
    val eventSink: (OnboardingUiEvent) -> Unit,
) : CircuitUiState

sealed interface OnboardingUiEvent{

}
