package app.shapeshifter.feature.workout.ui.trackworkout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.resources.Dimens
import app.shapeshifter.common.ui.compose.screens.TrackWorkoutScreen
import app.shapeshifter.data.models.workoutlog.ExerciseSession
import app.shapeshifter.data.models.workoutlog.SetLog
import app.shapeshifter.feature.workout.ui.components.AddNewSet
import app.shapeshifter.feature.workout.ui.components.ExerciseLog
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

@OptIn(ExperimentalFoundationApi::class)
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
            val overlayHost = LocalOverlayHost.current

            val startTime by remember(state.workoutSession?.workout?.startTimeInMillis) {
                val time = state.workoutSession?.workout?.startTimeInMillis
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
            )

            HorizontalDivider(
                thickness = 2.dp,
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    bottom = paddingValues.calculateBottomPadding(),
                ),
            ) {
                item("spacer") {
                    Spacer(
                        modifier = Modifier
                            .padding(vertical = 8.dp),
                    )
                }


                state.workoutSession?.exercises?.forEach { exerciseSession ->
                    exerciseLog(
                        exerciseSession = exerciseSession,
                        onCompleteSet = { isComplete, setLog ->
                            state.eventSink(TrackWorkoutUiEvent.OnSetCompleted(isComplete, setLog))
                        },
                        onAddSet = {
                            state.eventSink(TrackWorkoutUiEvent.OnAddSet(it))
                        },
                    )
                }

                if (state.workoutSession?.exercises.isNullOrEmpty()) {
                    item("empty_exercises_placeholder") {
                        ExerciseEmpty(
                            modifier = Modifier
                                .padding(top = Dimens.Spacing.Medium)
                                .fillMaxWidth()
                                .animateItemPlacement(),
                            onAddExercise = {
                                state.eventSink(TrackWorkoutUiEvent.OnAddExercise)
                            },
                        )
                    }
                }

                item("add_exercise_action") {
                    AddExercise(
                        onAddExercise = {
                            state.eventSink(TrackWorkoutUiEvent.OnAddExercise)
                        },
                        modifier = Modifier
                            .padding(horizontal = Dimens.Spacing.Medium),
                    )
                }

                item("discard_action") {
                    val scope = rememberCoroutineScope()
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
                            .padding(horizontal = Dimens.Spacing.Medium)
                            .padding(vertical = Dimens.Spacing.Medium),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.exerciseLog(
    exerciseSession: ExerciseSession,
    onCompleteSet: (Boolean, SetLog) -> Unit,
    onAddSet: (exerciseLogId: Long) -> Unit,
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

    items(
        items = exerciseSession.sets,
        contentType = { "set" },
        key = { "set_${it.id}" },
    ) { set ->
        SetLog(
            setLog = set,
            onComplete = onCompleteSet,
            modifier = Modifier
                .animateItemPlacement(),
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
                .animateItemPlacement()
                .padding(vertical = Dimens.Spacing.Small),
        )
    }

    itemDivider(
        key = "divider_${exerciseLog.id}",
        modifier = Modifier
            .padding(vertical = Dimens.Spacing.Medium),
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
                    vertical = Dimens.Spacing.Medium,
                )
                .padding(
                    end = Dimens.Spacing.Medium,
                    start = Dimens.Spacing.Small,
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
                    onBack()
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
                .height(Dimens.Spacing.Medium),
        )

        Text(
            style = MaterialTheme.typography.labelMedium,
            text = "Add an exercise to get started",
        )

        Spacer(
            modifier = Modifier
                .height(Dimens.Spacing.Medium),
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
        val minutes = ticks / 60
        val seconds = ticks % 60

        mutableStateOf("${minutes}m: ${seconds}s")
    }

    Text(
        text = minutesAndSeconds,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.SemiBold,
    )
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
