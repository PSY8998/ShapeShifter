package app.shapeshifter.feature.onboarding.data

data class Onboarding(
    val title: String,
    val description: String,
)

val onboardingPages = listOf(
    Onboarding("Welcome", "Are you ready"),
    Onboarding("User info", "Weight and Height")
)
