package app.shapeshifter.feature.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.shapeshifter.Clock
import app.shapeshifter.common.ui.compose.resources.Dimens
import app.shapeshifter.common.ui.compose.resources.Fire
import app.shapeshifter.common.ui.compose.resources.Medal
import app.shapeshifter.common.ui.compose.screens.ProfileScreen
import app.shapeshifter.data.models.workoutlog.ExerciseSession
import app.shapeshifter.data.models.workoutlog.WorkoutSession
import app.shapeshifter.feature.workout.ui.drawable.MoreHorizontal
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject

@Inject
class ProfileUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is ProfileScreen -> {
                ui<ProfileUiState> { state, modifier ->
                    Profile(state, modifier)
                }
            }

            else -> null
        }
    }
}

@Composable
fun Profile(
    state: ProfileUiState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            ProfileTopBar(
                modifier = Modifier
                    .fillMaxWidth(),
            )

            PreviousWorkouts(
                state.workouts,
            )
        }
    }
}

@Composable
fun ProfileTopBar(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            "Username",
            modifier = Modifier
                .weight(1f),
        )
        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.TopEnd),
        ) {
            ThreeDotMenu()
        }
    }
}

@Composable
fun ThreeDotMenu(
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(
        onClick = { expanded = true },
    ) {
        Icon(
            imageVector = MoreHorizontal,
            contentDescription = "Options",
        )
    }

}

@Composable
fun PreviousWorkouts(
    workoutSessions: List<WorkoutSession>,
    modifier: Modifier = Modifier,
) {
    LazyColumn {
        items(
            items = workoutSessions,
        ) { workoutSession ->
            WorkoutCard(workoutSession)
        }
    }
}

@Composable
fun WorkoutCard(
    workoutSession: WorkoutSession,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.elevatedCardElevation(Dimens.Padding.Small),
        shape = RoundedCornerShape(Dimens.Padding.Medium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        Column(
            modifier = Modifier,
        ) {
            Row(
                modifier = Modifier
                    .padding(Dimens.Padding.Medium),
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                ) {
                    Text(
                        text = workoutSession.workoutLog.name,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(
                        modifier = Modifier,
                    )

                    Text(
                        text = workoutSession.workoutLog.formatMillisToDate(),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }

                ThreeDotMenu(
                    modifier = Modifier,
                )
            }

            WorkoutSummary()

            WorkoutSummaryExercises(
                exerciseSessions = workoutSession.exerciseSessions,
                modifier = Modifier
                    .padding(Dimens.Padding.Medium),
            )
        }
    }
}

@Composable
fun WorkoutSummary(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.Padding.Medium),
        horizontalArrangement = Arrangement.spacedBy(
            Dimens.Padding.Medium
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                Dimens.Padding.ExtraSmall,
            ),
            modifier = Modifier
                .background(
                    color = Color(0xFFFBFBFB).copy(
                        alpha = 0.1f,
                    ),
                    shape = MaterialTheme.shapes.small,
                )
                .padding(Dimens.Padding.Medium)
                .weight(1f),
        ) {
            Icon(
                imageVector = Clock,
                contentDescription = "Duration",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp),
            )
            Text(
                text = "52 min",
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                Dimens.Padding.ExtraSmall,
            ),
            modifier = Modifier
                .background(
                    color = Color(0xFFFF4545).copy(
                        alpha = 0.1f,
                    ),
                    shape = MaterialTheme.shapes.small,
                )
                .padding(Dimens.Padding.Medium)
                .weight(1f),
        ) {
            Icon(
                imageVector = Fire,
                contentDescription = "Calories",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp),
            )
            Text(
                text = "640 kcal",
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                Dimens.Padding.ExtraSmall,
            ),
            modifier = Modifier
                .background(
                    color = Color(0xFF7ED4AD).copy(
                        alpha = 0.1f,
                    ),
                    shape = MaterialTheme.shapes.small,
                )
                .padding(Dimens.Padding.Medium)
                .weight(1f)
        ) {
            Icon(
                imageVector = Medal,
                contentDescription = "Records",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp),
            )
            Text(
                text = "3 records",
            )
        }

    }
}

@Composable
fun WorkoutSummaryExercises(
    exerciseSessions: List<ExerciseSession>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        exerciseSessions.forEach { exerciseSession ->
            Column(
                modifier = Modifier,
            ) {
                Text(
                    text = exerciseSession.exercise.name,
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    text = exerciseSession.setsOverview(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray,
                )

                HorizontalDivider(
                    thickness = 2.dp,
                )
            }
        }
    }
}

