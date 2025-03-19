package app.shapeshifter.feature.onboarding.ui

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class OnboardingUiState(
    val eventSink: (OnboardingUiEvent) -> Unit,
) : CircuitUiState

sealed interface OnboardingUiEvent: CircuitUiEvent {

    data object OnFinish : OnboardingUiEvent
}
