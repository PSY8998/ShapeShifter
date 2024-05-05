package app.shapeshifter.feature.workout.ui.savedworkouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import  org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.NestedScaffold
import app.shapeshifter.common.ui.compose.screens.SavedWorkoutsScreen
import app.shapeshifter.common.ui.compose.theme.Procelain
import app.shapeshifter.feature.workout.ui.drawable.MoreHorizontal
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.ExperimentalResourceApi
import shapeshifter.feature.workout.ui.generated.resources.Res
import shapeshifter.feature.workout.ui.generated.resources.barbell_overhead_empty

@Inject
class SavedWorkoutsUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is SavedWorkoutsScreen -> ui<SavedWorkoutsUiState> { state, modifier ->
                SavedWorkouts(state)
            }

            else -> null
        }
    }
}

@Composable
private fun SavedWorkouts(
    uiState: SavedWorkoutsUiState,
) {
    val eventSink = uiState.eventSink

    NestedScaffold(
        modifier = Modifier
            .fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            WorkoutTopBar(
                modifier = Modifier
                    .fillMaxWidth(),
            )

            SavedWorkoutsScrollingContent(
                onStart = {
                    eventSink(SavedWorkoutsUiEvent.OpenQuickWorkout)
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
            )
        }
    }
}

@Composable
private fun SavedWorkoutsScrollingContent(
    onStart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item("quick_workout") {
            QuickWorkout(
                onStart = onStart,
            )
        }

        item("routines") {
            Routines(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp),
            )
        }

        item("routines_divider") {
            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
            )
        }

        item("my_routines") {
            MyRoutine(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp),
            )
        }

        item("workout_divider") {
            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
            )
        }
    }
}

@Composable
private fun QuickWorkout(
    onStart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = "Quick Workout",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = "Add exercises and perform workout",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            modifier = Modifier,
        )

        Button(
            onClick = {
                onStart()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = MaterialTheme.shapes.small,
        ) {
            Text(
                "Start empty workout",
                style = MaterialTheme.typography.titleSmall,
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
        )
    }
}

@Composable
private fun Routines(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            Text(
                text = "Routines",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier,
            )

            Text(
                text = "A set of workout to perform",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier,
            )

            Text(
                text = "1 routine",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier,
            )
        }

        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = MaterialTheme.shapes.small,
                )
                .clip(shape = MaterialTheme.shapes.small)
                .clickable {

                }
                .padding(4.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create a new routine",
                modifier = Modifier
                    .align(Alignment.Center),
                tint = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun MyRoutine(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "My Workouts",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f),
            )

            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = MaterialTheme.shapes.small,
                    )
                    .clip(shape = MaterialTheme.shapes.small)
                    .clickable {

                    }
                    .padding(4.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create a new workout",
                    modifier = Modifier
                        .align(Alignment.Center),
                    tint = MaterialTheme.colorScheme.onSecondary,
                )
            }

            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clip(shape = MaterialTheme.shapes.small)
                    .clickable { }
                    .padding(4.dp),
            ) {
                Icon(
                    imageVector = MoreHorizontal,
                    contentDescription = "",
                    modifier = Modifier
                        .align(Alignment.Center),
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Image(
                painter = painterResource(Res.drawable.barbell_overhead_empty),
                contentDescription = "empty workout",
            )

            Text(
                text = "No workouts present in this routine",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier,
                color = Color.Gray,
            )
        }
    }

}

@Composable
private fun WorkoutTopBar(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                "Start Workout",
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                    .align(Alignment.Center),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
        )
    }
}
