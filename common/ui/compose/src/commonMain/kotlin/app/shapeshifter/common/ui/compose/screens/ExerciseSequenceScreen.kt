package app.shapeshifter.common.ui.compose.screens

import app.shapeshifter.data.models.workoutlog.ExerciseSession
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ExerciseSequenceScreen(
    val exerciseSessions : @RawValue List<ExerciseSession>
): Screen
