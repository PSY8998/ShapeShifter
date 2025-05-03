package app.shapeshifter.feature.root.ui

import app.shapeshifter.feature.onboarding.data.OnboardingPreferences
import me.tatarka.inject.annotations.Inject

typealias RootViewModelFactory = () -> RootViewModel

@Inject
class RootViewModel(
    onboardingPreferences: OnboardingPreferences,
){
    val isOnboardingCompleted = onboardingPreferences.isCompleted
}
