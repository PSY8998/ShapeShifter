package app.shapeshifter.feature.exercise.ui.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.NestedScaffold
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.vectorResource
import shapeshifter.feature.exercise.ui.generated.resources.Res
import shapeshifter.feature.exercise.ui.generated.resources.barbell_overhead

@Inject
class ExercisesUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is ExercisesScreen -> ui<ExercisesState> { state, _ ->
                Exercises(state)
            }

            else -> null
        }
    }
}

@Composable
fun Exercises(
    state: ExercisesState,
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    Exercises(
        state = state,
        openCreateExerciseScreen = { eventSink(ExerciseUiEvent.OpenCreateExercise) },
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun Exercises(
    state: ExercisesState,
    openCreateExerciseScreen: () -> Unit,
) {
    NestedScaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ExercisesTopBar()

            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(
                    space = 16.dp,
                    alignment = Alignment.CenterVertically,
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    imageVector = vectorResource(Res.drawable.barbell_overhead),
                    contentDescription = "barbell overhead",
                )

                Text(
                    text = "Get moving! Create your first exercise today " +
                        "and begin your fitness journey.",
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                )

                Button(
                    onClick = {
                        openCreateExerciseScreen()
                    },
                    modifier = Modifier,
                ) {
                    Text("Create Exercise")
                }
            }
        }
    }
}

@Composable
private fun ExercisesTopBar(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            "Exercises",
            modifier = Modifier
                .padding(8.dp),
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}