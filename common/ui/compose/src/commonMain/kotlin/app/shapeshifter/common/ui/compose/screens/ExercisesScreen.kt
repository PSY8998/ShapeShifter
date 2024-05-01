package app.shapeshifter.common.ui.compose.screens

import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExercisesScreen(
    val canSelect: Boolean = false,
) : Screen {

    @Parcelize
    data class Result(val exerciseIds: List<Long>) : PopResult
}
