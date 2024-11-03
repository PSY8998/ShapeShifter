package app.shapeshifter.feature.workout.ui.finishworkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.shapeshifter.common.ui.compose.resources.Dimens
import app.shapeshifter.common.ui.compose.screens.FinishWorkoutScreen
import app.shapeshifter.feature.workout.ui.finishworkout.components.DateSelector
import app.shapeshifter.feature.workout.ui.finishworkout.components.FinishWorkoutTopSection
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject

@Inject
class FinishWorkoutUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is FinishWorkoutScreen -> ui<FinishWorkoutUiState> { state, modifier ->
                FinishWorkout(state, modifier)
            }

            else -> null
        }
    }
}

@Composable
fun FinishWorkout(
    state: FinishWorkoutUiState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues),
        ) {
            FinishWorkoutTopSection(
                workoutName = "Quick Workout",
                modifier = Modifier,
            )

            DateSelector(
                modifier = Modifier
                    .padding(top = Dimens.Padding.Medium),
            )
        }
    }
}
