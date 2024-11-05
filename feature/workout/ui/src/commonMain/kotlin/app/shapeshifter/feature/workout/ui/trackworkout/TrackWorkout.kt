package app.shapeshifter.feature.workout.ui.trackworkout

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.resources.Dimens
import app.shapeshifter.common.ui.compose.screens.TrackWorkoutScreen
import app.shapeshifter.common.ui.compose.ui.Crossfade
import app.shapeshifter.data.models.PositiveInt
import app.shapeshifter.data.models.workoutlog.ExerciseSession
import app.shapeshifter.data.models.workoutlog.SetLog
import app.shapeshifter.feature.workout.ui.components.AddNewSet
import app.shapeshifter.feature.workout.ui.components.ExerciseLog
import app.shapeshifter.feature.workout.ui.components.SetAnchorBox
import app.shapeshifter.feature.workout.ui.components.SetColumnTitles
import app.shapeshifter.feature.workout.ui.components.SetLog
import app.shapeshifter.feature.workout.ui.components.showDiscardWorkoutDialog
import com.slack.circuit.overlay.LocalOverlayHost
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import com.slack.circuitx.overlays.DialogResult
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.painterResource
import shapeshifter.feature.workout.ui.generated.resources.Res
import shapeshifter.feature.workout.ui.generated.resources.ic_dumbbell_workout
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Inject
class TrackWorkoutUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is TrackWorkoutScreen -> ui<TrackWorkoutUiState> { state, modifier ->
                TrackWorkout(state)
            }

            else -> null
        }
    }
}

@Composable
private fun TrackWorkout(
    state: TrackWorkoutUiState,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxSize(),
        ) {
            val startTime by remember(state.asFilled()?.workoutSession?.workoutLog?.startTimeInMillis) {
                val time = state.asFilled()?.workoutSession?.workoutLog?.startTimeInMillis
                if (time == null) {
                    mutableLongStateOf(0L)
                } else {
                    mutableLongStateOf(
                        (System.currentTimeMillis() - time) / 1000,
                    )
                }
            }

            TrackWorkoutTopBar(
                modifier = Modifier
                    .fillMaxWidth(),
                startTimeInSecs = startTime,
                onBack = {
                    state.eventSink(TrackWorkoutUiEvent.GoBack)
                },
                onFinish = {
                    val workoutSession = state.asFilled()?.workoutSession
                    if (workoutSession != null) {
                        state.eventSink(TrackWorkoutUiEvent.OnFinishWorkout(workoutSession))
                    }
                },
            )

            HorizontalDivider(
                thickness = 2.dp,
            )

            Crossfade(
                targetState = state,
                modifier = Modifier
                    .fillMaxSize(),
                label = "ScreenTransition",
                animationSpec = tween(
                    durationMillis = 600,
                    easing = FastOutSlowInEasing,
                ),
                contentKey = { it::class.java },
            ) { targetState ->
                when (targetState) {
                    is TrackWorkoutUiState.Empty -> {
                        ExerciseEmpty(
                            modifier = Modifier
                                .padding(top = Dimens.Padding.Medium)
                                .fillMaxWidth(),
                            onAddExercise = {
                                state.eventSink(TrackWorkoutUiEvent.OnAddExercise)
                            },
                            onDiscardWorkout = {
                                state.eventSink(TrackWorkoutUiEvent.DiscardWorkout)
                            },
                        )
                    }

                    is TrackWorkoutUiState.Filled -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentPadding = PaddingValues(
                                top = Dimens.Padding.Medium,
                                bottom = paddingValues.calculateBottomPadding()
                                    + Dimens.Padding.Largest,
                            ),
                        ) {

                            targetState.workoutSession.exerciseSessions.forEach { exerciseSession ->
                                exerciseLog(
                                    exerciseSession = exerciseSession,
                                    onCompleteSet = {
                                        state.eventSink(TrackWorkoutUiEvent.OnSetCompleted(it))
                                    },
                                    onAddSet = {
                                        state.eventSink(
                                            TrackWorkoutUiEvent.OnAddSet(
                                                exerciseLogId = it,
                                                exerciseId = exerciseSession.exercise.id,
                                                workoutPlanId = targetState.workoutSession.workoutLog.workoutPlanId,
                                                workoutLogId = targetState.workoutSession.workoutLog.id,
                                            ),
                                        )
                                    },
                                    onDeleteSet = {
                                        state.eventSink(TrackWorkoutUiEvent.OnDeleteSet(it))
                                    },
                                )
                            }

                            item("add_exercise_action") {
                                AddExercise(
                                    onAddExercise = {
                                        state.eventSink(TrackWorkoutUiEvent.OnAddExercise)
                                    },
                                    modifier = Modifier
                                        .padding(horizontal = Dimens.Padding.Medium),
                                )
                            }

                            item("discard_action") {
                                val scope = rememberCoroutineScope()
                                val overlayHost = LocalOverlayHost.current

                                DiscardWorkout(
                                    onDiscardWorkout = {
                                        scope.launch {
                                            val result = overlayHost.showDiscardWorkoutDialog()
                                            if (result == DialogResult.Confirm) {
                                                state.eventSink(TrackWorkoutUiEvent.DiscardWorkout)
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = Dimens.Padding.Medium)
                                        .padding(vertical = Dimens.Padding.Medium),
                                )
                            }
                        }
                    }

                    is TrackWorkoutUiState.Initial -> {}
                }
            }
        }
    }
}

private fun LazyListScope.exerciseLog(
    exerciseSession: ExerciseSession,
    onCompleteSet: (setLog: SetLog) -> Unit,
    onAddSet: (exerciseLogId: Long) -> Unit,
    onDeleteSet: (setLog: SetLog) -> Unit,
) {
    val exerciseLog = exerciseSession.exerciseLog
    item(
        key = "exercise_${exerciseLog.id}",
        contentType = "exercise",
    ) {
        ExerciseLog(
            name = exerciseSession.exercise.name,
        )
    }

    item(
        key = "set_titles_${exerciseLog.id}",
        contentType = "set_titles",
    ) {
        SetColumnTitles(
            modifier = Modifier
                .fillMaxWidth(),
        )
    }

    itemsIndexed(
        items = exerciseSession.sets,
        contentType = { _, _ -> "set" },
        key = { _, set -> "set_${set.id}" },
    ) { index, set ->
        SetAnchorBox(
            backgroundContent = { progress ->
                Box(
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.errorContainer),
                ) {
                    // Define the start and end offset for the slide animation
                    val startOffset = 80.dp // Start 100.dp to the right (adjust as needed)
                    val endOffset = 0.dp     // End position is 0.dp (the original position)

                    var targetOffset by remember { mutableStateOf(startOffset) }

                    // Animate the offset based on targetOffset
                    val animatedOffset by animateDpAsState(
                        targetValue = targetOffset,
                        animationSpec = tween(
                            easing = FastOutSlowInEasing,
                        ),
                        label = "DeleteTransition",
                    )

                    // Trigger animation only once when progress reaches 1.0
                    LaunchedEffect(progress) {
                        if (progress == 1.0f) {
                            targetOffset = endOffset
                        }

                        if (progress == 0f) {
                            targetOffset = startOffset
                        }
                    }

                    val density = LocalDensity.current

                    IconButton(
                        modifier = Modifier
                            .padding(horizontal = Dimens.Padding.Medium)
                            .offset {
                                IntOffset(
                                    x = with(density) {
                                        animatedOffset
                                            .toPx()
                                            .toInt()
                                    },
                                    y = 0,
                                )
                            },
                        onClick = {
                            onDeleteSet(set)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove Set",
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                        )
                    }
                }
            },
            content = {
                SetLog(
                    index = index,
                    weight = set.weight.value,
                    reps = set.reps.value,
                    isChecked = set.finishTime > 0,
                    onCheckChanged = { isChecked, weight, reps ->
                        onCompleteSet(
                            set.copy(
                                weight = PositiveInt(weight),
                                reps = PositiveInt(reps),
                                finishTime = if (isChecked) System.currentTimeMillis() else 0,
                            ),
                        )
                    },
                    prevReps = set.prevReps.value,
                    prevWeight = set.prevWeight.value,
                    isBeingTracked = true,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background),
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .animateItem(
                    fadeInSpec = null,
                    fadeOutSpec = null,
                ),
        )
    }

    item(
        key = "${exerciseLog.id}_new_set",
        contentType = "new_set",
    ) {
        AddNewSet(
            onAddSet = {
                onAddSet(exerciseLog.id)
            },
            modifier = Modifier
                .animateItem()
                .padding(vertical = Dimens.Padding.Small),
        )
    }

    itemDivider(
        key = "divider_${exerciseLog.id}",
        modifier = Modifier
            .padding(vertical = Dimens.Padding.Medium),
    )
}

private fun LazyListScope.itemDivider(
    key: Any? = null,
    modifier: Modifier = Modifier,
) {
    item(key) {
        HorizontalDivider(
            thickness = 1.dp,
            modifier = modifier
                .fillParentMaxWidth(),
        )
    }
}

@Composable
private fun TrackWorkoutTopBar(
    modifier: Modifier = Modifier,
    startTimeInSecs: Long,
    onBack: () -> Unit,
    onFinish: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Column(
        modifier = modifier
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = Dimens.Padding.Medium,
                )
                .padding(
                    end = Dimens.Padding.Medium,
                    start = Dimens.Padding.Small,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {
                    onBack()
                },
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(align = Alignment.Start),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    modifier = Modifier,
                    contentDescription = "",
                )
            }

            Column(
                modifier = Modifier
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    "Track Workout",
                    modifier = Modifier,
                )

                WorkoutTimer(
                    startTimeInSecs = startTimeInSecs,
                )
            }

            Button(
                onClick = {
                    onFinish()
                },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(align = Alignment.End),
                contentPadding = PaddingValues(
                    vertical = 8.dp,
                    horizontal = 16.dp,
                ),
            ) {
                Text("Finish")
            }
        }
    }
}

@Composable
private fun ExerciseEmpty(
    modifier: Modifier = Modifier,
    onAddExercise: () -> Unit,
    onDiscardWorkout: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_dumbbell_workout),
            "",
            modifier = Modifier,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        )

        Text(
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            text = "Get Started",
        )

        Spacer(
            modifier = Modifier
                .height(Dimens.Padding.Medium),
        )

        Text(
            style = MaterialTheme.typography.labelMedium,
            text = "Add an exercise to get started",
        )

        Spacer(
            modifier = Modifier
                .height(Dimens.Padding.Medium),
        )

        AddExercise(
            onAddExercise = {
                onAddExercise()
            },
            modifier = Modifier
                .padding(horizontal = Dimens.Padding.Medium),
        )

        val overlayHost = LocalOverlayHost.current
        val scope = rememberCoroutineScope()

        DiscardWorkout(
            onDiscardWorkout = {
                scope.launch {
                    val result = overlayHost.showDiscardWorkoutDialog()
                    if (result == DialogResult.Confirm) {
                        onDiscardWorkout()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.Padding.Medium)
                .padding(vertical = Dimens.Padding.Medium),
        )
    }
}

@Composable
fun AddExercise(
    onAddExercise: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        onClick = {
            onAddExercise()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
        ),
    ) {
        Text("+ Add Exercise")
    }
}

@Composable
fun WorkoutTimer(startTimeInSecs: Long) {
    var ticks by rememberSaveable(startTimeInSecs) { mutableLongStateOf(startTimeInSecs) }
    LaunchedEffect(startTimeInSecs) {
        while (true) {
            delay(1.seconds)
            ticks++
        }
    }

    val minutesAndSeconds by remember(ticks) {
        mutableStateOf(naturalTimeSpent(ticks))
    }

    Text(
        text = minutesAndSeconds,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.SemiBold,
    )
}

fun naturalTimeSpent(timeSpent: Long): String {
    val days = timeSpent / (24 * 3600)
    val hours = (timeSpent % (24 * 3600)) / 3600
    val minutes = (timeSpent % 3600) / 60
    val seconds = timeSpent % 60

    return buildString {
        if (days > 0) append("${days}d:")
        if (hours > 0 || days > 0) append("${hours}h:")
        if (minutes > 0 || hours > 0 || days > 0) append("${minutes}m:")
        append("${seconds}s")
    }
}


@Composable
private fun DiscardWorkout(
    onDiscardWorkout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        onClick = {
            onDiscardWorkout()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.error,
        ),
    ) {
        Text("Discard Workout")
    }
}
