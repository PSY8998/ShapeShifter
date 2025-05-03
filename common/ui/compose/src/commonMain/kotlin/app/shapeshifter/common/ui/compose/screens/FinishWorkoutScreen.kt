package app.shapeshifter.common.ui.compose.screens

import com.slack.circuit.runtime.screen.Screen

@Parcelize
data class FinishWorkoutScreen(
    val workoutId: Long,
) : Screen
