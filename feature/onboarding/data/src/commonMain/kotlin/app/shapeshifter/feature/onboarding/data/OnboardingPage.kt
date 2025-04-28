package app.shapeshifter.feature.onboarding.data

data class OnboardingPage(
    val title: String,
    val description: String,
)

val onboardingPages = listOf(
    OnboardingPage("Welcome", "Your personal workout companion."),
    OnboardingPage("Pick Your Goal", "What's your top priority?"),
    OnboardingPage("Track & Analyze", "See your progress at a glance."),
    OnboardingPage("Plan & Quick Starts", "Create custom plans or hit play instantly."),
)
