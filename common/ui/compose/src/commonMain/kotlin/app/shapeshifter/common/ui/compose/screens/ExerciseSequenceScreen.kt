package app.shapeshifter.common.ui.compose.screens

import com.slack.circuit.runtime.screen.Screen

@Parcelize
data class ExerciseSequenceScreen(
    val workoutLogId : Long
): Screen
