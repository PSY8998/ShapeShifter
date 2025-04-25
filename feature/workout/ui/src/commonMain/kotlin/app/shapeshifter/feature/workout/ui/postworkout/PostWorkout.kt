package app.shapeshifter.feature.workout.ui.postworkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle // Checkmark icon
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share // Icon for Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.shapeshifter.common.ui.compose.resources.Medal
import app.shapeshifter.common.ui.compose.screens.PostWorkoutScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject

// Define Colors (adjust as needed to match image)
private val DarkBackground = Color(0xFF121212)
private val CardBackground = Color(0xFF1E1E1E) // Slightly lighter dark grey for cards
private val TitlePurple = Color(0xFFD0BCFF) // A purple accent for titles
private val CheckmarkGreen = Color(0xFF4CAF50)
private val ProgressTrackColor = Color.Gray.copy(alpha = 0.3f)
private val ProgressCyan = Color(0xFF00BCD4) // Adjusted cyan
private val ProgressOrange = Color(0xFFFF9800) // Adjusted orange/red

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
    // Provide default content color for dark background
    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) { padding ->
        CompositionLocalProvider(LocalContentColor provides Color.White) {
            Surface(
                modifier = modifier.fillMaxSize(),
                color = DarkBackground, // Dark background for the whole screen
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = padding.calculateTopPadding()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Header: Checkmark + Title
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Completed",
                            tint = CheckmarkGreen,
                            modifier = Modifier.size(32.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Workout Complete",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Duration Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(vertical = 24.dp, horizontal = 16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = state.totalTime,
                                style = MaterialTheme.typography.displayLarge, // Large text for time
                                fontWeight = FontWeight.Bold,
                                fontSize = 56.sp, // Explicitly large font size
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Total Duration",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray, // Subdued color for label
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Exercises Completed Section
                    SectionTitle("Exercises Completed")
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp), // Padding for row edges
                    ) {
                        items(state.completedExercises, key = { it.id }) { exercise ->
                            ExerciseCard(exercise = exercise)
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Stats Section (Sets & Calories)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly, // Space out the two cards
                    ) {
                        // Calculate progress fraction (assuming max is arbitrary for now)
                        val setsProgress =
                            (state.setCount / 20f).coerceIn(0f, 1f) // Example: Max 20 sets
                        val caloriesProgress =
                            (state.caloriesBurned?.toFloat()?.div(1000f) ?: 0f).coerceIn(
                                0f,
                                1f,
                            ) // Example: Max 1000 kcal

                        StatProgressCard(
                            value = state.setCount.toString(),
                            label = "Sets",
                            progress = setsProgress,
                            color = ProgressCyan,
                            modifier = Modifier.weight(1f), // Equal weight
                        )
                        Spacer(modifier = Modifier.width(16.dp)) // Space between cards
                        StatProgressCard(
                            value = state.caloriesBurned?.toString() ?: "--",
                            label = "Calories",
                            progress = caloriesProgress,
                            color = ProgressOrange,
                            modifier = Modifier.weight(1f), // Equal weight
                        )
                    }


                    // Records Section (Conditional)
                    if (state.achievedRecords.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(32.dp))
                        SectionTitle("Records")
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            state.achievedRecords.forEach { record ->
                                RecordItem(record = record)
                            }
                        }
                    }

                    // Push buttons to the bottom
                    Spacer(modifier = Modifier.weight(1f))

                    // Bottom Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly, // Space out buttons
                    ) {
                        SmallActionButton(
                            text = "Log Notes",
                            icon = Icons.Filled.Notifications,
                            onClick = { state.eventSink(PostWorkoutEvent.LogNotes) },
                        )
                        SmallActionButton(
                            text = "Share",
                            icon = Icons.Filled.Share,
                            onClick = { state.eventSink(PostWorkoutEvent.ShareProgress) },
                        )
                    }
                }
            }
        }
    }
}

// Helper for Section Titles
@Composable
private fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = TitlePurple,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 4.dp), // Slight indent for title
    )
}

// Helper for Exercise Cards in the LazyRow
@Composable
private fun ExerciseCard(exercise: ExerciseInfo, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.size(width = 110.dp, height = 110.dp), // Square-ish card
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center, // Center content vertically
        ) {
            Icon(
                imageVector = Icons.Filled.Person, // Placeholder Icon
                contentDescription = exercise.name,
                modifier = Modifier.size(40.dp), // Larger icon
                tint = LocalContentColor.current.copy(alpha = 0.8f), // Slightly muted icon
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                maxLines = 2, // Allow wrapping
            )
        }
    }
}

// Helper for Stat Cards with Circular Progress
@Composable
private fun StatProgressCard(
    value: String,
    label: String,
    progress: Float, // 0.0f to 1.0f
    color: Color,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.aspectRatio(1f), // Make it square
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp), // Padding inside card
            contentAlignment = Alignment.Center, // Center everything in the Box
        ) {
            // Background track indicator
            CircularProgressIndicator(
                progress = { 1f }, // Full circle
                modifier = Modifier.matchParentSize(), // Fill the box
                color = ProgressTrackColor,
                strokeWidth = 8.dp, // Adjust thickness
                strokeCap = StrokeCap.Round,
            )
            // Actual progress indicator
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.matchParentSize(),
                color = color,
                strokeWidth = 8.dp,
                strokeCap = StrokeCap.Round,
            )
            // Text content inside the circle
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp, // Adjust font size as needed
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                )
            }
        }
    }
}

// Helper for Record Items
@Composable
private fun RecordItem(record: RecordInfo, modifier: Modifier = Modifier) {
    // Using a Row directly instead of Card for a simpler look like the image
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Medal,
            contentDescription = "Records",
            tint = Color.Unspecified,
            modifier = Modifier
                .size(24.dp),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = record.name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.weight(1f)) // Push value to the end
        Text(
            text = record.value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}

// Helper for smaller action buttons at the bottom
@Composable
private fun SmallActionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        // Use TextButton for less emphasis
        onClick = onClick,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp), // Smaller padding
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = text, modifier = Modifier.size(18.dp)) // Smaller icon
            Spacer(Modifier.width(4.dp))
            Text(text, style = MaterialTheme.typography.labelMedium) // Smaller text
        }
    }
}
