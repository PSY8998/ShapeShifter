package app.shapeshifter.feature.onboarding.data

data class OnboardingPage(
    val title: String,
    val description: String,
)


val onboardingPages = listOf(
    OnboardingPage("Welcome", "Let's get started!"), // Updated description
    OnboardingPage("Age", "What's your age?"), // New Age step
    OnboardingPage("Weight", "What's your weight?"), // New Weight step
    OnboardingPage("Height", "What's your height?"), // New Height step
    OnboardingPage("Start your journey", "You're all set!") // Updated description
)
