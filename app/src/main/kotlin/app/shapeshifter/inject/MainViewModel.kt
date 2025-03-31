package app.shapeshifter.inject

import androidx.lifecycle.ViewModel
import app.shapeshifter.feature.onboarding.data.OnboardingPreferences
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.map

typealias MainViewModelFactory = () -> MainViewModel

@Inject
class MainViewModel(
    private val onboardingPreferences: OnboardingPreferences,
): ViewModel(){

    val isOnboardingCompleted = onboardingPreferences.isCompleted
}
