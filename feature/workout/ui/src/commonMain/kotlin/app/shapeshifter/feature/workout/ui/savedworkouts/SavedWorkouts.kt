package app.shapeshifter.feature.workout.ui.savedworkouts

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.NestedScaffold
import app.shapeshifter.common.ui.compose.resources.Dimens
import app.shapeshifter.common.ui.compose.screens.SavedWorkoutsScreen
import app.shapeshifter.data.models.workoutlog.WorkoutLog
import app.shapeshifter.data.models.workoutlog.WorkoutSessionOverview
import app.shapeshifter.feature.workout.ui.components.showDiscardWorkoutDialog
import app.shapeshifter.feature.workout.ui.createworkoutplan.WorkoutPlanNameResult
import app.shapeshifter.feature.workout.ui.createworkoutplan.showWorkoutPlanName
import app.shapeshifter.feature.workout.ui.drawable.MoreHorizontal
import com.slack.circuit.overlay.LocalOverlayHost
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import com.slack.circuitx.overlays.DialogResult
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import shapeshifter.feature.workout.ui.generated.resources.Res
import shapeshifter.feature.workout.ui.generated.resources.barbell_overhead_empty
import kotlinx.coroutines.launch

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
internal fun SavedWorkouts(
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
        ) {
            WorkoutTopBar(
                activeWorkout = uiState.activeWorkout,
                onDiscardWorkout = {
                    eventSink(SavedWorkoutsUiEvent.DiscardWorkout(it))
                },
                onResumeWorkout = {
                    eventSink(SavedWorkoutsUiEvent.OpenQuickWorkout)
                },
                modifier = Modifier
                    .fillMaxWidth(),
            )

            val overlayHost = LocalOverlayHost.current
            val scope = rememberCoroutineScope()

            SavedWorkoutsScrollingContent(
                onStartQuickWorkout = {
                    if (uiState.activeWorkout != null) {
                        scope.launch {
                            val result = overlayHost.showDiscardWorkoutDialog()
                            if (result == DialogResult.Confirm) {
                                eventSink(SavedWorkoutsUiEvent.DiscardAndStartNewWorkout(uiState.activeWorkout.workout))
                            }
                        }
                    } else {
                        eventSink(SavedWorkoutsUiEvent.OpenQuickWorkout)
                    }
                },
                onCreateWorkoutPlan = { routineId, planName ->
                    eventSink(SavedWorkoutsUiEvent.CreateWorkoutPlan(routineId, planName))
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
    onStartQuickWorkout: () -> Unit,
    onCreateWorkoutPlan: (
        routineId: Long,
        planName: String,
    ) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = Dimens.Spacing.Medium),
    ) {
        item("quick_workout") {
            QuickWorkout(
                onStart = onStartQuickWorkout,
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
                onCreateWorkoutPlan = onCreateWorkoutPlan,
                modifier = Modifier
                    .fillParentMaxHeight()
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

@Composable
private fun MyRoutine(
    onCreateWorkoutPlan: (
        routineId: Long,
        planName: String,
    ) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        val overlayHost = LocalOverlayHost.current
        val scope = rememberCoroutineScope()

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
                        scope.launch {
                            val result = overlayHost.showWorkoutPlanName()
                            if (result is WorkoutPlanNameResult.AddExercisesToPlan) {
                                onCreateWorkoutPlan(-1, result.name)
                            }
                        }
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
    activeWorkout: WorkoutSessionOverview?,
    onDiscardWorkout: (workoutLog: WorkoutLog) -> Unit,
    onResumeWorkout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 16.dp),
        ) {
            Text(
                text = if (activeWorkout == null) "Start Workout" else "Workout in progress",
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                    .align(Alignment.CenterHorizontally),
            )


            AnimatedContent(
                targetState = activeWorkout,
                contentKey = { it?.workout?.id },
                label = "resume_workout_transition",
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth(),
            ) { workoutSession ->
                if (workoutSession != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Dimens.Spacing.ExtraSmall),
                    ) {
                        Text(
                            "${workoutSession.routine.name} ${workoutSession.plan.name}",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth(),
                        )

                        val overlayHost = LocalOverlayHost.current
                        val scope = rememberCoroutineScope()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing.Medium),
                        ) {
                            Button(
                                onClick = {
                                    scope.launch {
                                        val result = overlayHost.showDiscardWorkoutDialog()
                                        if (result == DialogResult.Confirm) {
                                            onDiscardWorkout(workoutSession.workout)
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.error,
                                ),
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier
                                    .weight(1f),
                            ) {
                                Text("Discard")
                            }

                            Button(
                                onClick = {
                                    onResumeWorkout()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                ),
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier
                                    .weight(1f),
                            ) {
                                Text("Resume")
                            }
                        }
                    }
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
        )
    }
}
