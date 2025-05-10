package app.shapeshifter.feature.workout.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import app.shapeshifter.Clock
import app.shapeshifter.common.ui.compose.resources.Dimens
import app.shapeshifter.common.ui.compose.ui.animatableListItems
import app.shapeshifter.data.models.ExerciseTemplate
import app.shapeshifter.data.models.SetType
import app.shapeshifter.data.models.workout.Exercise
import app.shapeshifter.data.models.workout.ExerciseSession
import app.shapeshifter.data.models.workout.Set
import app.shapeshifter.data.models.workout.SetLog
import app.shapeshifter.feature.workout.ui.drawable.MoreHorizontal
import coil3.compose.AsyncImage
import kotlin.math.roundToInt

@Composable
fun Exercise(
    exerciseSession: ExerciseSession,
    isInEditMode: Boolean,
    isInLoggingMode: Boolean,
    onDelete: (Set) -> Unit,
    onAddSet: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        ExerciseDetail(
            exercise = exerciseSession.exercise,
            exerciseTemplate = exerciseSession.exerciseTemplate,
            isInEditMode = isInEditMode,
            onShowExerciseActions = {},
            onSetRestTimer = {},
        )

        Sets(
            sets = exerciseSession.sets,
            isInEditMode = isInEditMode,
            isInLoggingMode = isInLoggingMode,
            onDeleteSet = onDelete,
            onAddSet = onAddSet,
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun ExerciseDetail(
    exercise: Exercise,
    exerciseTemplate: ExerciseTemplate,
    isInEditMode: Boolean,
    onShowExerciseActions: () -> Unit,
    onSetRestTimer: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Padding.Medium),
    ) {
        AsyncImage(
            model = exerciseTemplate.imageUrl,
            contentDescription = "${exerciseTemplate.name} image",
            modifier = Modifier
                .padding(start = Dimens.Padding.Medium)
                .clip(shape = RoundedCornerShape(8.dp))
                .width(48.dp)
                .aspectRatio(2 / 3f)
                .background(Color.White),
        )

        Column(
            modifier = Modifier
                .weight(1f, fill = true),
        ) {
            Text(
                text = exerciseTemplate.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            val noteTextFieldState = rememberTextFieldState(
                initialText = exercise.note ?: "",
            )

            BasicTextField(
                enabled = isInEditMode,
                state = noteTextFieldState,
                textStyle = MaterialTheme.typography.bodySmall
                    .copy(
                        color = Color.Gray,
                    ),
                cursorBrush = SolidColor(Color.Gray),
                decorator = { innerTextField ->
                    if (noteTextFieldState.text.isEmpty()) {
                        Text(
                            text = "Go slow and control each movement",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                        )
                    }

                    innerTextField()
                },
                lineLimits = TextFieldLineLimits.MultiLine(
                    minHeightInLines = 1,
                    maxHeightInLines = 3,
                ),
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }

        IconButton(
            onClick = { onSetRestTimer() },
        ) {
            Icon(
                imageVector = Clock,
                tint = Color.Unspecified,
                contentDescription = "Rest Time Duration",
                modifier = Modifier
                    .size(24.dp),
            )
        }

        IconButton(
            onClick = { onShowExerciseActions() },
            modifier = Modifier,
        ) {
            Icon(
                imageVector = MoreHorizontal,
                contentDescription = "Options",
            )
        }
    }
}

@Composable
private fun Sets(
    sets: List<Set>,
    isInEditMode: Boolean,
    isInLoggingMode: Boolean,
    onDeleteSet: (Set) -> Unit,
    onAddSet: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        SetsHeader(
            isInLoggingMode = isInLoggingMode,
            modifier = Modifier.fillMaxWidth(),
        )

        animatableListItems(sets, id = { it.id }).forEach { animatableItem ->
            key(animatableItem.id) {
                AnimatedVisibility(visibleState = animatableItem.transitionState) {
                    SetDragBox(
                        set = animatableItem.item,
                        isInEditMode = isInEditMode,
                        isInLoggingMode = isInLoggingMode,
                        onDeleteSet = onDeleteSet,
                        modifier = Modifier
                            .animateEnterExit(
                                enter = fadeIn(),
                                exit = fadeOut(),
                            ),
                    )
                }
            }
        }

        if (isInEditMode) {
            AddNewSet(
                onAddSet = onAddSet,
                modifier = Modifier
                    .padding(top = Dimens.Padding.Small),
            )
        }
    }
}

@Composable
private fun SetsHeader(
    isInLoggingMode: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.Padding.Small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Set",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            modifier = Modifier
                .weight(1f),
        )

        Text(
            text = "Prev",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f),
        )

        Text(
            text = "Kg",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f),
        )

        Text(
            text = "Reps",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f),
        )


        if (isInLoggingMode) {
            Box(
                modifier = Modifier
                    .weight(1f),
            )
        }

    }
}

@Composable
private fun SetDragBox(
    set: Set,
    isInEditMode: Boolean,
    isInLoggingMode: Boolean,
    onDeleteSet: (Set) -> Unit,
    modifier: Modifier = Modifier,
) {
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
            Set(
                set = set,
                isInEditMode = isInEditMode,
                isInLoggingMode = isInLoggingMode,
                onShowSetActions = {},
                modifier = Modifier,
            )
        },
        enabled = isInEditMode,
        modifier = modifier
            .fillMaxWidth(),
    )
}

@Composable
private fun Set(
    set: Set,
    isInEditMode: Boolean,
    isInLoggingMode: Boolean,
    onShowSetActions: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        var isCompleted by remember {
            val initialCompletedState = isInLoggingMode &&
                set is SetLog &&
                set.isCompleted

            mutableStateOf(initialCompletedState)
        }

        val pattern = Regex("^\\d*$")

        val setType = remember(set.setTypeId) {
            SetType.fromId(set.setTypeId.toLong())
        }

        Row(
            modifier = modifier
                .defaultMinSize(minHeight = 48.dp)
                .fillMaxWidth()
                .then(
                    if (isCompleted) {
                        Modifier.background(MaterialTheme.colorScheme.secondary)
                    } else
                        Modifier
                            .background(MaterialTheme.colorScheme.background),
                )
                .padding(vertical = Dimens.Padding.Small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(end = 8.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .background(
                                    color = Color(setType.colorHex).copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(4.dp),
                                )
                                .border(
                                    width = 1.dp,
                                    color = Color(setType.colorHex),
                                    shape = RoundedCornerShape(4.dp),
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = setType.abbreviation,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(setType.colorHex),
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    // Show the set number
                    Text(
                        text = (set.index + 1).toString(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            Text(
                text = "0 kg" + " x " + "0",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier
                    .weight(1f),
            )

            var setWeight: String by remember(set.weight.value) {
                val initialWeight = set.weight.value.takeIf { it != 0f }?.toString() ?: ""
                mutableStateOf(initialWeight)
            }

            BasicTextField(
                enabled = isInEditMode,
                value = setWeight,
                onValueChange = {
                    if (pattern.matchEntire(it) != null) {
                        setWeight = it
                    }
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Black,
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .wrapContentWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (setWeight.isBlank()) {
                            Text(
                                text = "0",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Black,
                                ),
                                modifier = Modifier
                                    .fillMaxSize(),
                            )
                        }

                        innerTextField()
                    }
                },
                modifier = Modifier
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .weight(1f)
                    .width(IntrinsicSize.Min)
                    .defaultMinSize(24.dp),
            )

            var setReps by remember(set.reps.value) {
                mutableStateOf(set.reps.value.takeIf { it != 0 }?.toString() ?: "")
            }

            BasicTextField(
                enabled = isInEditMode,
                value = setReps,
                onValueChange = {
                    if (pattern.matchEntire(it) != null) {
                        setReps = it
                    }
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Black,
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .wrapContentWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (setReps.isBlank()) {
                            Text(
                                text = "0",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Black,
                                ),
                                modifier = Modifier
                                    .fillMaxSize(),
                            )
                        }

                        innerTextField()
                    }
                },
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                ),
                modifier = Modifier
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .weight(1f)
                    .width(IntrinsicSize.Min)
                    .defaultMinSize(24.dp),
            )

            if (isInLoggingMode) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                        .background(
                            color = if (isCompleted) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.secondary,
                            shape = MaterialTheme.shapes.small,
                        )
                        .clip(MaterialTheme.shapes.small)
                        .toggleable(
                            value = isCompleted,
                            onValueChange = {
                                isCompleted = it
                            },
                        )
                        .padding(Dimens.Padding.ExtraSmall),
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Complete Set",
                        tint = if (isCompleted) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSecondary,
                    )
                }
            }
        }
    }
}

enum class SetAnchors {
    SELECTED,
    UNSELECTED,
    OVERSCROLL,
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SetAnchorBox(
    backgroundContent: @Composable RowScope.(progress: Float) -> Unit,
    content: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val density = LocalDensity.current

    val state = rememberAnchorDraggableState(
        initialValue = SetAnchors.UNSELECTED,
        positionalThreshold = { totalDistance: Float -> totalDistance * 0.5f },
        velocityThreshold = { with(density) { 100.dp.toPx() } },
        decayAnimationSpec = exponentialDecay(
            frictionMultiplier = 1f,
        ),
        snapAnimationSpec = spring(
            stiffness = Spring.StiffnessMedium,
            dampingRatio = Spring.DampingRatioMediumBouncy,
        ),
        confirmValueChange = {
            it != SetAnchors.OVERSCROLL
        },
    ).apply {
        updateAnchors(
            newAnchors = DraggableAnchors {
                with(density) {
                    SetAnchors.UNSELECTED at 0.dp.toPx()
                    SetAnchors.SELECTED at -80.dp.toPx()
                    SetAnchors.OVERSCROLL at -180.dp.toPx()
                }
            },
        )
    }

    Box(
        modifier = modifier
            .height(IntrinsicSize.Max),
        propagateMinConstraints = true,
    ) {
        Row(
            content = {
                val selectedProgress = state.progress(SetAnchors.UNSELECTED, SetAnchors.SELECTED)
                backgroundContent(selectedProgress)
            },
            modifier = Modifier,
        )
        Row(
            content = content,
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = state
                            .requireOffset()
                            .roundToInt(),
                        y = 0,
                    )
                }
                .anchoredDraggable(
                    state = state,
                    orientation = Orientation.Horizontal,
                    enabled = enabled,
                ),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun <T : Any> rememberAnchorDraggableState(
    initialValue: T,
    snapAnimationSpec: AnimationSpec<Float>,
    decayAnimationSpec: DecayAnimationSpec<Float>,
    positionalThreshold: (distance: Float) -> Float,
    velocityThreshold: () -> Float,
    confirmValueChange: (T) -> Boolean = { true },
): AnchoredDraggableState<T> {
    return rememberSaveable(
        saver = AnchoredDraggableState.Saver(
            snapAnimationSpec = snapAnimationSpec,
            decayAnimationSpec = decayAnimationSpec,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
            confirmValueChange = confirmValueChange,
        ),
    ) {
        AnchoredDraggableState(
            initialValue = initialValue,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
            decayAnimationSpec = decayAnimationSpec,
            snapAnimationSpec = snapAnimationSpec,
            confirmValueChange = confirmValueChange,
        )
    }
}

@Composable
fun AddNewSet(
    onAddSet: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(align = Alignment.CenterHorizontally)
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = MaterialTheme.shapes.small,
                )
                .clip(shape = MaterialTheme.shapes.small)
                .clickable {
                    onAddSet()
                }
                .padding(4.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
            )
        }

        Box(
            modifier = Modifier
                .weight(1f),
        )

        Box(
            modifier = Modifier
                .weight(1f),
        )

        Box(
            modifier = Modifier
                .weight(1f),
        )

        Box(
            modifier = Modifier
                .weight(1f),
        )
    }
}
