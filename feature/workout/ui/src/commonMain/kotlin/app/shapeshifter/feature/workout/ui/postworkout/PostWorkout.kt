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
import app.shapeshifter.common.ui.compose.theme.DarkBackground
import app.shapeshifter.common.ui.compose.theme.DarkPrimary
import app.shapeshifter.common.ui.compose.theme.DarkSecondary
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject

// Theme colors
private val AccentPurple = Color(0xFFD0BCFF) // Keeping purple for accents
private val ProgressCyan = Color(0xFF03DAC5) // Material design cyan
private val ProgressOrange = Color(0xFFFF8800) // Adjusted orange
private val ProgressTrackColor = DarkPrimary.copy(alpha = 0.2f)

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
        CompositionLocalProvider(LocalContentColor provides DarkPrimary) {
            Surface(
                modifier = modifier.fillMaxSize(),
                color = DarkBackground, // Using theme background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(top = padding.calculateTopPadding()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Header: Checkmark + Title
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Completed",
                            tint = ProgressCyan,
                            modifier = Modifier.size(32.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Workout Complete",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Duration Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = DarkSecondary),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
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
                                color = DarkPrimary.copy(alpha = 0.7f), // Slightly subdued
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Exercises Completed Section
                    SectionTitle("Exercises Completed")
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp), // Padding for row edges
                    ) {
                        items(state.completedExercises, key = { it.id }) { exercise ->
                            ExerciseCard(exercise = exercise)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

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
                        Spacer(modifier = Modifier.height(24.dp))
                        SectionTitle("Records")
                        Spacer(modifier = Modifier.height(12.dp))
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            state.achievedRecords.forEach { record ->
                                RecordItem(record = record)
                            }
                        }
                    }

                    // Push buttons to the bottom
                    Spacer(modifier = Modifier.weight(1f))

                    // Bottom Action Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
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
        color = AccentPurple,
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
        modifier = modifier.size(width = 120.dp, height = 110.dp), // Slightly wider cards
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = DarkSecondary),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center, // Center content vertically
        ) {
            Icon(
                imageVector = Icons.Filled.Person, // Placeholder Icon
                contentDescription = exercise.name,
                modifier = Modifier.size(40.dp), // Larger icon
                tint = DarkPrimary, // Using theme color
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.bodyMedium,
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
        modifier = modifier
            .aspectRatio(1f) // Make it square
            .padding(4.dp), // Add some padding around the card
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = DarkSecondary),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Padding inside card
            contentAlignment = Alignment.Center, // Center everything in the Box
        ) {
            // Background track indicator
            CircularProgressIndicator(
                progress = { 1f }, // Full circle
                modifier = Modifier.matchParentSize(), // Fill the box
                color = ProgressTrackColor,
                strokeWidth = 10.dp, // Thicker stroke
                strokeCap = StrokeCap.Round,
            )
            // Actual progress indicator
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.matchParentSize(),
                color = color,
                strokeWidth = 10.dp,
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
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkPrimary.copy(alpha = 0.7f),
                )
            }
        }
    }
}

// Helper for Record Items
@Composable
private fun RecordItem(record: RecordInfo, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 2.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSecondary.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Medal,
                contentDescription = "Record",
                tint = Color.Unspecified,
                modifier = Modifier.size(28.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
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
}

// Helper for smaller action buttons at the bottom
@Composable
private fun SmallActionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = DarkSecondary,
            contentColor = DarkPrimary,
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = text, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(text, style = MaterialTheme.typography.labelLarge)
        }
    }
}
