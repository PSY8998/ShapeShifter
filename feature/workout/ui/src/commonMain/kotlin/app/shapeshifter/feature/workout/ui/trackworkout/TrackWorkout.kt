package app.shapeshifter.feature.workout.ui.trackworkout

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.resources.Dimens
import app.shapeshifter.common.ui.compose.screens.TrackWorkoutScreen
import app.shapeshifter.common.ui.compose.ui.Crossfade
import app.shapeshifter.data.models.SetType
import app.shapeshifter.data.models.workout.ExerciseLog
import app.shapeshifter.data.models.workout.ExerciseLogSession
import app.shapeshifter.data.models.workout.Reps
import app.shapeshifter.data.models.workout.SetLog
import app.shapeshifter.data.models.workout.Weight
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
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
            val startTime by remember(state.asFilled()?.workoutSession?.workout?.startTime) {
                val time = state.asFilled()?.workoutSession?.workout?.startTime?.toEpochMilliseconds()
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
                state = state,
                onBack = {
                    state.eventSink(TrackWorkoutUiEvent.GoBack)
                },
                contentPadding = PaddingValues(),
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
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                        ) {
                            LazyColumn(
                                modifier = Modifier,
                                contentPadding = PaddingValues(
                                    top = Dimens.Padding.Medium,
                                    bottom = paddingValues.calculateBottomPadding() + Dimens.Padding.Largest,
                                ),
                            ) {
                                targetState.workoutSession.exerciseSessions.forEach { exerciseSession ->
                                    exerciseLog(
                                        exerciseSession = exerciseSession,
                                        onCompleteSet = {
                                            state.eventSink(
                                                TrackWorkoutUiEvent.OnSetCompleted(
                                                    set = it,
                                                    exerciseLog = exerciseSession.exercise,
                                                ),
                                            )
                                        },
                                        onAddSet = {
                                            state.eventSink(
                                                TrackWorkoutUiEvent.OnAddSet(
                                                    exerciseLogId = it,
                                                    exerciseId = exerciseSession.exercise.id,
                                                    workoutPlanId = targetState.workoutSession.workout.workoutPlanId,
                                                    workoutLogId = targetState.workoutSession.workout.id,
                                                ),
                                            )
                                        },
                                        onDeleteSet = {
                                            state.eventSink(TrackWorkoutUiEvent.OnDeleteSet(it))
                                        },
                                        onReorderExercises = {
                                            state.eventSink(
                                                TrackWorkoutUiEvent
                                                    .OnReorderExercises(
                                                        targetState.workoutSession.workout.id,
                                                    ),
                                            )
                                        },
                                        onRemoveExercise = {
                                            state.eventSink(
                                                TrackWorkoutUiEvent.OnRemoveExercise(
                                                    exerciseSession.exercise,
                                                ),
                                            )
                                        },
                                        onUpdateRestTime = { minutes, seconds ->
                                            state.eventSink(
                                                TrackWorkoutUiEvent.OnUpdateRestTime(
                                                    minutes, seconds,
                                                    exerciseLog = exerciseSession.exercise,
                                                ),
                                            )
                                        },

                                        onReplaceExercise = {
                                            state.eventSink(
                                                TrackWorkoutUiEvent.OnReplaceExercise(
                                                    exerciseLog = exerciseSession.exercise,
                                                ),
                                            )
                                        },
                                        onUpdateSet = {
                                            state.eventSink(
                                                TrackWorkoutUiEvent.OnUpdateSet(
                                                    it,
                                                ),
                                            )
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

                            if (targetState.restTimeDurationInSecs > 0) {
                                RestTimer(
                                    restTimeDurationInSecs = targetState.restTimeDurationInSecs / 1000,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(Dimens.Padding.Medium)
                                        .align(Alignment.BottomCenter),
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
    exerciseSession: ExerciseLogSession,
    onCompleteSet: (setLog: SetLog) -> Unit,
    onAddSet: (exerciseLogId: Long) -> Unit,
    onDeleteSet: (setLog: SetLog) -> Unit,
    onReorderExercises: (exerciseLogId: Long) -> Unit,
    onReplaceExercise: (exerciseLog: ExerciseLog) -> Unit,
    onRemoveExercise: (exerciseLog: ExerciseLog) -> Unit,
    onUpdateRestTime: (Int, Int) -> Unit,
    onUpdateSet: (setLog: SetLog) -> Unit,
) {
    val exerciseLog = exerciseSession.exercise

    item(
        key = "exercise_${exerciseLog.id}",
        contentType = "exercise",
    ) {
        ExerciseLog(
            name = exerciseSession.exerciseTemplate.name,
            onReorderExercises = { onReorderExercises(exerciseLog.id) },
            onReplaceExercise = { onReplaceExercise(exerciseLog) },
            onRemoveExercise = { onRemoveExercise(exerciseLog) },
            updateRestTime = onUpdateRestTime,
            modifier = Modifier,
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
                    weight = set.weight.value.toInt(),
                    reps = set.reps.value,
                    isChecked = set.isCompleted,
                    onCheckChanged = { isChecked, weight, reps ->
                        onCompleteSet(
                            set.copy(
                                weight = Weight(weight.toFloat()),
                                reps = Reps(reps),
                                isCompleted = isChecked,
                            ),
                        )
                    },
                    prevReps = Reps.ZERO.value,
                    prevWeight = Weight.ZERO.value.toInt(),
                    isBeingTracked = true,
                    setType = SetType.fromId(set.setTypeId.toLong()),
                    onSetTypeChange = { newType ->
                        onUpdateSet(set.copy(setTypeId = newType.id.toInt()))
                    },
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
    state: TrackWorkoutUiState,
    onBack: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Left side button with weight 1
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

        // Center column with weight 2
        Column(
            modifier = Modifier
                .weight(2f) // Use more weight to keep it centered
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

        // Right side button with weight 1
        Box(modifier = Modifier.weight(1f)) {
            val filledState = state.asFilled()
            if (filledState != null) {
                TrackWorkoutFinishButtonAction(filledState)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit,
) {
    var isVisible by remember { mutableStateOf(true) }

    if (!isVisible) {
        onDismiss()
        return
    }

    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = {
            it != SheetValue.Hidden
        },
    )

    val currentTimeMillis = remember { System.currentTimeMillis() }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentTimeMillis,
        initialDisplayMode = DisplayMode.Input,
    )

    // Time selection state
    val calendar = remember { Calendar.getInstance() }
    calendar.timeInMillis = currentTimeMillis

    var hour by remember { mutableIntStateOf(calendar.get(Calendar.HOUR_OF_DAY)) }
    var minute by remember { mutableIntStateOf(calendar.get(Calendar.MINUTE)) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
        ),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(horizontal = Dimens.Padding.Medium, vertical = Dimens.Padding.Small),
            contentPadding = PaddingValues(bottom = Dimens.Padding.ExtraLarge),
        ) {
            item(key = "title") {
                Text(
                    text = "Set workout date",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = Dimens.Padding.Medium),
                )
            }

            item(key = "date_selector") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Dimens.Padding.Medium),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        value = datePickerState.selectedDateMillis?.let {
                            val dateFormat = SimpleDateFormat(
                                "MMM dd, yyyy",
                                Locale.getDefault(),
                            )
                            dateFormat.format(Date(it))
                        } ?: "",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Date") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    datePickerState.displayMode =
                                        DisplayMode.Picker
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.DateRange,
                                    contentDescription = "Show calendar",
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        ),
                    )
                }
            }

            item(key = "date_picker") {
                if (datePickerState.displayMode == DisplayMode.Picker) {
                    DatePicker(
                        state = datePickerState,
                        modifier = Modifier.fillMaxWidth(),
                        colors = DatePickerDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            titleContentColor = MaterialTheme.colorScheme.onSurface,
                            headlineContentColor = MaterialTheme.colorScheme.onSurface,
                            weekdayContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            subheadContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            yearContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            currentYearContentColor = MaterialTheme.colorScheme.primary,
                            selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
                            selectedYearContainerColor = MaterialTheme.colorScheme.primary,
                            dayContentColor = MaterialTheme.colorScheme.onSurface,
                            selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                            selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                            todayContentColor = MaterialTheme.colorScheme.primary,
                            todayDateBorderColor = MaterialTheme.colorScheme.primary,
                        ),
                    )
                }
            }

            item(key = "time_selector") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.Padding.Medium),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Time: ",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(end = Dimens.Padding.Small),
                    )

                    OutlinedTextField(
                        value = hour.toString(),
                        onValueChange = { newValue ->
                            val newHour = newValue.toIntOrNull()
                            if (newHour != null && newHour in 0..23) {
                                hour = newHour
                            }
                        },
                        modifier = Modifier.width(100.dp),
                        label = { Text("Hour") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        ),
                    )

                    Text(
                        text = ":",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = Dimens.Padding.ExtraSmall),
                    )

                    OutlinedTextField(
                        value = minute.toString(),
                        onValueChange = { newValue ->
                            val newMinute = newValue.toIntOrNull()
                            if (newMinute != null && newMinute in 0..59) {
                                minute = newMinute
                            }
                        },
                        modifier = Modifier.width(100.dp),
                        label = { Text("Min") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        ),
                    )
                }
            }

            item(key = "action_buttons") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = Dimens.Padding.Medium,
                            bottom = Dimens.Padding.Large,
                        ),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = {
                            isVisible = false
                        },
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            try {
                                val selectedDate =
                                    datePickerState.selectedDateMillis
                                        ?: System.currentTimeMillis()

                                val resultCalendar = Calendar.getInstance()
                                resultCalendar.timeInMillis = selectedDate

                                resultCalendar.set(Calendar.HOUR_OF_DAY, hour.coerceIn(0, 23))
                                resultCalendar.set(Calendar.MINUTE, minute.coerceIn(0, 59))
                                resultCalendar.set(Calendar.SECOND, 0)
                                resultCalendar.set(Calendar.MILLISECOND, 0)

                                onConfirm(resultCalendar.timeInMillis)
                            } catch (e: Exception) {
                                // If there's any exception when setting date/time,
                                // just use current time as fallback
                                onConfirm(System.currentTimeMillis())
                            }
                            isVisible = false
                        },
                        modifier = Modifier.padding(start = Dimens.Padding.Small),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Composable
fun RestTimer(
    restTimeDurationInSecs: Long,
    modifier: Modifier = Modifier,
) {

    var isTimerRunning by remember { mutableStateOf(false) }
    var remainingTime by remember { mutableLongStateOf(0) }

    val progress = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(restTimeDurationInSecs) {
        remainingTime = restTimeDurationInSecs
        isTimerRunning = true
    }

    LaunchedEffect(restTimeDurationInSecs) {
        progress.snapTo(0f)
        progress.animateTo(
            1f,
            animationSpec = tween(
                durationMillis = restTimeDurationInSecs.toInt() * 1000,
                easing = LinearEasing,
            ),
        )
    }

    if (isTimerRunning) {
        LaunchedEffect(Unit) {
            while (remainingTime > 0) {
                delay(1000L)
                remainingTime -= 1
            }
            isTimerRunning = false
        }
    }

    Card(
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.6f)
                        .padding(Dimens.Padding.ExtraMedium),
                ) {
                    Text(
                        text = "Rest Timer",
                        color = Color.Gray,
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Padding.ExtraSmall),
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(color = MaterialTheme.colorScheme.onSurface)
                                .padding(
                                    vertical = Dimens.Padding.Smallest,
                                    horizontal = Dimens.Padding.ExtraSmall,
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "-15",
                                color = Color.Black,
                            )
                        }
                        Text(
                            text = "${(remainingTime / 60)}:" +
                                "${remainingTime % 60}",
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier
                                .padding(top = Dimens.Padding.Small),
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(color = MaterialTheme.colorScheme.onSurface)
                                .padding(vertical = 2.dp, horizontal = Dimens.Padding.ExtraSmall),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "+15",
                                color = Color.Black,
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(Dimens.Padding.Small)
                        .weight(0.4f)
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.4f,
                            ),
                            shape = MaterialTheme.shapes.extraLarge,
                        ),
                ) {
                    CircularProgressIndicator(
                        progress = { progress.value },
                        color = MaterialTheme.colorScheme.onSurface,
                        strokeWidth = 16.dp,
                        strokeCap = StrokeCap.Round,
                        trackColor = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.2f,
                        ),
                        modifier = Modifier
                            .padding(Dimens.Padding.Small)
                            .fillMaxSize(),
                    )

                    Box(
                        modifier = Modifier
                            .padding(Dimens.Padding.Large)
                            .fillMaxSize()
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainer,
                                shape = CircleShape,
                            )
                            .align(Alignment.Center),
                    ) {
                        Image(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Start rest timer",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.Center),
                        )
                    }
                }
            }
        }

    }

}

@Composable
private fun TrackWorkoutFinishButtonAction(
    state: TrackWorkoutUiState.Filled,
) {
    var showDatePicker by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        Button(
            onClick = { showDatePicker = true },
            shape = MaterialTheme.shapes.small,
            contentPadding = PaddingValues(
                vertical = 8.dp,
                horizontal = 16.dp,
            ),
        ) {
            Text("Finish")
        }
    }

    if (showDatePicker) {
        DatePickerBottomSheet(
            onDismiss = { showDatePicker = false },
            onConfirm = { selectedDate ->
                showDatePicker = false
                state.eventSink(
                    TrackWorkoutUiEvent.OnFinishWorkoutWithDate(
                        workoutSession = state.workoutSession,
                        selectedDate = selectedDate,
                    ),
                )
            },
        )
    }
}
