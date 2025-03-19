package app.shapeshifter.feature.onboarding.data

data class OnboardingPage(
    val title: String,
    val description: String,
)

val onboardingPages = listOf(
    OnboardingPage("Welcome", "Are you ready"),
    OnboardingPage("User info", "Weight and Height"),
    OnboardingPage("Start your journey", "Have fun")
)
