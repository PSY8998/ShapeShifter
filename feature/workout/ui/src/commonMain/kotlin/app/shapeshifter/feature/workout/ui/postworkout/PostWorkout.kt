package app.shapeshifter.feature.workout.ui.postworkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.resources.Medal
import app.shapeshifter.common.ui.compose.screens.PostWorkoutScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject

@Inject
class PostWorkoutUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is PostWorkoutScreen -> ui<PostWorkoutUiState> { state, modifier ->
                PostWorkout(state, modifier)
            }

            else -> null
        }
    }
}

@Composable
fun PostWorkout(
    state: PostWorkoutUiState,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // Push buttons to bottom
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Headline
                Text(
                    text = "Workout Complete! 💪", // Or "You Crushed It!", "Muscles Thank You!"
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Summary Stats Card
                Card(modifier = Modifier.padding(horizontal = 8.dp)) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Summary", style = MaterialTheme.typography.titleMedium)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Total Time")
                            Text(" Total Time: ${state.totalTime}", style = MaterialTheme.typography.bodyLarge)
                        }
                        state.caloriesBurned?.let {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Calories Burned") // Replace with better icon
                                Text(" Calories Burned: $it kcal", style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Exercises")
                            Text(" Exercises: ${state.exerciseCount}", style = MaterialTheme.typography.bodyLarge)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Sets")
                            Text(" Sets: ${state.setCount}", style = MaterialTheme.typography.bodyLarge)
                        }
                        if (state.hasNewPR) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Medal,
                                    contentDescription = "Records",
                                    tint = Color.Unspecified,
                                    modifier = Modifier
                                        .size(24.dp),
                                )
                                Text(" New Personal Record Achieved!", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Placeholder for Motivational Quote or Confetti Animation
                Text(
                    text = "\"The body achieves what the mind believes.\" - Napoleon Hill", // Placeholder quote
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                // TODO: Add Confetti animation here if desired

                Spacer(modifier = Modifier.height(24.dp))

                // Placeholder for Mini Graph or Streak Tracker
                // TODO: Implement graph/streak component here
                 Text(
                    text = "[Mini Graph/Streak Tracker Placeholder]",
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                )
            }


            // Buttons / CTAs at the bottom
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                 Button(onClick = { state.eventSink(PostWorkoutEvent.LogNotes) }) {
                    Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text("Log Notes / Rate Workout")
                }
                Button(onClick = { state.eventSink(PostWorkoutEvent.ShareProgress) }) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text("Share Progress") // TODO: Implement sharing logic
                }
                Button(onClick = { state.eventSink(PostWorkoutEvent.ViewHistory) }) {
                    Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text("View Workout History")
                }
                Button(onClick = { state.eventSink(PostWorkoutEvent.ReturnHome) }) {
                    Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text("Return to Home")
                }
            }
        }
    }
}
