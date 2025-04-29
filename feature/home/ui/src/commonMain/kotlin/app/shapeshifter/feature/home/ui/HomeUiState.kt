package app.shapeshifter.feature.home.ui

import com.slack.circuit.runtime.CircuitUiState

data class NextWorkoutInfo(
    val name: String = "",
    val scheduledFor: String = "",
    val duration: String = ""
)

data class WeeklyStatistics(
    val caloriesBurnt: Int = 0,
    val weightChange: Float = 0f,
    val focusedMuscles: List<String> = emptyList()
)

data class HomeUiState(
    val userName: String = "Athlete",
    val nextWorkout: NextWorkoutInfo? = NextWorkoutInfo(
        name = "Upper Body Strength",
        scheduledFor = "Today, 5:00 PM",
        duration = "45 min"
    ),
    val currentWeight: Float = 70f,
    val goalWeight: Float = 65f,
    val weeklyStatistics: WeeklyStatistics = WeeklyStatistics(
        caloriesBurnt = 3500,
        weightChange = -0.5f,
        focusedMuscles = listOf("Chest", "Back", "Arms")
    )
) : CircuitUiState
