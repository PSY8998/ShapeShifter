@file:Suppress("ktlint:filename")

package app.shapeshifter.feature.exercise.ui.exercises

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.NestedScaffold
import app.shapeshifter.common.ui.compose.resources.Dimens
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import app.shapeshifter.data.models.Exercise
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.vectorResource
import shapeshifter.feature.exercise.ui.generated.resources.Res
import shapeshifter.feature.exercise.ui.generated.resources.barbell_overhead
import kotlin.math.roundToInt

@Inject
class ExercisesUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is ExercisesScreen -> ui<ExercisesUiState> { uiState, _ ->
                Exercises(uiState)
            }

            else -> null
        }
    }
}

@Composable
private fun Exercises(
    uiState: ExercisesUiState,
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = uiState.eventSink

    NestedScaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ExercisesTopBar()

            Box(
                modifier = Modifier
                    .weight(1f),
            ) {
                when (uiState) {
                    is ExercisesUiState.Exercises -> {
                        ExercisesContent(
                            uiState = uiState,
                            openCreateExercise = {
                                eventSink(ExerciseUiEvent.OpenCreateExercise)
                            },
                            modifier = Modifier,
                        )
                    }

                    is ExercisesUiState.Empty -> {
                        ExercisesEmptyContent(
                            onCreateExercise = {
                                eventSink(ExerciseUiEvent.OpenCreateExercise)
                            },
                            modifier = Modifier
                                .align(Alignment.Center),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExercisesContent(
    uiState: ExercisesUiState.Exercises,
    openCreateExercise: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        ExerciseScrollContent(
            modifier = Modifier
                .fillMaxSize(),
            exercises = uiState.exercises,
        )

        Button(
            onClick = {
                openCreateExercise()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter),
        ) {
            Text("Create Exercise")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExerciseScrollContent(
    modifier: Modifier = Modifier,
    exercises: List<Exercise>,
) {
    var selectedExerciseIds by remember { mutableStateOf(emptySet<Long>()) }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.Spacing.Medium),
    ) {
        items(exercises) { exercise ->
            val exerciseSelected by remember {
                derivedStateOf {
                    selectedExerciseIds.contains(exercise.id)
                }
            }

            val exerciseSelectedIndex by remember {
                derivedStateOf {
                    if (exerciseSelected)
                        selectedExerciseIds.indexOf(exercise.id)
                    else -1
                }
            }

            val textStyle = MaterialTheme.typography.labelLarge.merge(
                color = Color.White,
            )

            val textMeasurer = rememberTextMeasurer()

            val measure = remember(exerciseSelectedIndex) {
                textMeasurer.measure(text = exerciseSelectedIndex.toString(), style = textStyle)
            }

            val state = rememberExerciseAnchorState()

            LaunchedEffect(exerciseSelected) {
                if (exerciseSelected) {
                    state.dismiss(ExerciseAnchors.SELECTED)
                } else {
                    state.dismiss(ExerciseAnchors.UNSELECTED)
                }
            }

            ExerciseAnchorBox(
                state = state,
                backgroundContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Red),
                    ) {
                        var displayIndex by remember { mutableStateOf("") }

                        LaunchedEffect(exerciseSelectedIndex) {
                            if (exerciseSelectedIndex != -1) {
                                displayIndex = (exerciseSelectedIndex + 1).toString()
                            }
                        }

                        Text(
                            text = displayIndex,
                            style = textStyle,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(horizontal = Dimens.Spacing.Small),
                        )
                    }
                },
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Black)
                            .clickable {
                                if (exerciseSelected) {
                                    selectedExerciseIds = selectedExerciseIds.minus(exercise.id)
                                } else {
                                    selectedExerciseIds = selectedExerciseIds.plus(exercise.id)
                                }
                            },
                    )
                },
                modifier = Modifier
                    .padding(horizontal = Dimens.Spacing.Medium)
                    .fillMaxWidth()
                    .height(100.dp),
            )

//            val exerciseSelectedIndex by remember {
//                derivedStateOf { if (exerciseSelected) selectedExerciseIds.indexOf(exercise.id) else -1 }
//            }
//
//            val textStyle = MaterialTheme.typography.labelLarge.merge(
//                color = Color.White,
//            )
//            val textMeasurer = rememberTextMeasurer()
//
//            val measure = remember(exerciseSelectedIndex) {
//                textMeasurer.measure(text = exerciseSelectedIndex.toString(), style = textStyle)
//            }
//
//            val offset by animateIntAsState(
//                if (exerciseSelected) measure.size.width + 64 else 0,
//                label = "",
//            )
//
//            val cardShifting by remember { derivedStateOf { offset != 0 } }
//
//            ElevatedCard(
//                elevation = CardDefaults.cardElevation(
//                    defaultElevation = 6.dp,
//                ),
//                modifier = Modifier
//                    .pointerInput(exerciseSelected) {
//                        detectTapGestures(
//                            onTap = {
//                                selectedExerciseIds = if (exerciseSelected) {
//                                    selectedExerciseIds.minus(element = exercise.id)
//                                } else {
//                                    selectedExerciseIds.plus(element = exercise.id)
//                                }
//                            },
//                        )
//                    }
//                    .fillMaxWidth()
//                    .height(100.dp)
//                    .padding(8.dp)
//                    .drawBehind {
//                        if (cardShifting) {
//                            drawRoundRect(
//                                color = Color.Black,
//                                cornerRadius = CornerRadius(x = 12.dp.toPx(), y = 12.dp.toPx()),
//                            )
//
//                            drawText(
//                                textLayoutResult = measure,
//                                color = Color.White,
//                                topLeft = Offset(
//                                    x = size.width - measure.size.width - 32,
//                                    y = size.height / 2 - measure.size.height / 2,
//                                ),
//                                alpha = 1f,
//                            )
//                        }
//                    }
//                    .offset {
//                        IntOffset(x = -offset.toInt(), 0)
//                    },
//            ) {
//                Row(
//                    modifier = Modifier,
//                    verticalAlignment = Alignment.CenterVertically,
//                ) {
//                    Image(
//                        painter = painterResource(Res.drawable.exercise_deadlift),
//                        contentDescription = "exercise image",
//                        modifier = Modifier,
//                    )
//
//                    Column(
//                        modifier = Modifier
//                            .padding(top = 8.dp)
//                            .align(Alignment.Top),
//                    ) {
//                        Text(
//                            modifier = Modifier
//                                .padding(horizontal = 8.dp),
//                            text = exercise.name,
//                        )
//
//                        Text(
//                            modifier = Modifier
//                                .padding(horizontal = 8.dp),
//                            text = exercise.primaryMuscle.displayName,
//                            color = Color.Gray,
//                            style = MaterialTheme.typography.labelMedium,
//                        )
//
//                        Text(
//                            modifier = Modifier
//                                .padding(horizontal = 8.dp),
//                            text = exercise.secondaryMuscle.joinToString("/ ") {
//                                it.displayName
//                            },
//                            color = Color.Gray,
//                            style = MaterialTheme.typography.labelMedium,
//                        )
//                    }
//                }
//            }
        }
    }
}

@Composable
private fun ExercisesTopBar(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            "Exercises",
            modifier = Modifier
                .padding(8.dp),
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ExercisesEmptyContent(
    modifier: Modifier = Modifier,
    onCreateExercise: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            imageVector = vectorResource(Res.drawable.barbell_overhead),
            contentDescription = "barbell overhead",
        )

        Text(
            text = "Get moving! Create your first exercise today " +
                "and begin your fitness journey.",
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(horizontal = 16.dp),
        )

        Button(
            onClick = {
                onCreateExercise()
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
        ) {
            Text("Create Exercise")
        }
    }
}

enum class ExerciseAnchors {
    SELECTED,
    UNSELECTED
}

@Composable
@ExperimentalMaterial3Api
fun rememberExerciseAnchorState(
    initialValue: ExerciseAnchors = ExerciseAnchors.UNSELECTED,
    positionalThreshold: (totalDistance: Float) -> Float = { 42f },
): ExerciseAnchorState {
    val density = LocalDensity.current
    return rememberSaveable(
        saver = ExerciseAnchorState.Saver(
            density = density,
            positionalThreshold = positionalThreshold,
        ),
    ) {
        ExerciseAnchorState(initialValue, density, positionalThreshold)
    }
}

@OptIn(ExperimentalFoundationApi::class)
class ExerciseAnchorState(
    initialValue: ExerciseAnchors,
    density: Density,
    positionalThreshold: (totalDistance: Float) -> Float,
) {
    internal val anchoredDraggableState = AnchoredDraggableState(
        initialValue = initialValue,
        animationSpec = tween(),
        confirmValueChange = { true },
        positionalThreshold = positionalThreshold,
        velocityThreshold = { with(density) { 148.dp.toPx() } },
    )

    val currentValue: ExerciseAnchors get() = anchoredDraggableState.currentValue

    val targetValue: ExerciseAnchors get() = anchoredDraggableState.targetValue

    internal val offset: Float get() = anchoredDraggableState.offset

    fun requireOffset(): Float = anchoredDraggableState.requireOffset()

    suspend fun dismiss(direction: ExerciseAnchors) {
        anchoredDraggableState.animateTo(targetValue = direction)
    }

    companion object {

        /**
         * The default [Saver] implementation for [SwipeToDismissBoxState].
         */
        fun Saver(
            positionalThreshold: (totalDistance: Float) -> Float,
            density: Density,
        ) = Saver<ExerciseAnchorState, ExerciseAnchors>(
            save = { it.currentValue },
            restore = {
                ExerciseAnchorState(
                    it, density, positionalThreshold,
                )
            },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ExerciseAnchorBox(
    state: ExerciseAnchorState,
    backgroundContent: @Composable RowScope.() -> Unit,
    content: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    Box(
        modifier
            .anchoredDraggable(
                state = state.anchoredDraggableState,
                orientation = Orientation.Horizontal,
                enabled = state.currentValue == ExerciseAnchors.UNSELECTED,
                reverseDirection = isRtl,
            ),
        propagateMinConstraints = true,
    ) {
        Row(
            content = backgroundContent,
            modifier = Modifier.matchParentSize(),
        )
        Row(
            content = content,
            modifier = Modifier
                .swipeToExerciseAnchors(state),
        )
    }
}

private fun Modifier.swipeToExerciseAnchors(
    state: ExerciseAnchorState,
) = this then ExerciseAnchorsElement(
    state,
)

private class ExerciseAnchorsElement(
    private val state: ExerciseAnchorState,
) : ModifierNodeElement<ExerciseAnchorsNode>() {

    override fun create() = ExerciseAnchorsNode(
        state,
    )

    override fun update(node: ExerciseAnchorsNode) {
        node.state = state
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        other as ExerciseAnchorsElement
        if (state != other.state) return false
        return true
    }

    override fun hashCode(): Int {
        val result = state.hashCode()
        return result
    }

    override fun InspectorInfo.inspectableProperties() {
        debugInspectorInfo {
            properties["state"] = state
        }
    }
}

private class ExerciseAnchorsNode(
    var state: ExerciseAnchorState,
) : Modifier.Node(), LayoutModifierNode {
    private var didLookahead: Boolean = false

    override fun onDetach() {
        didLookahead = false
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        // If we are in a lookahead pass, we only want to update the anchors here and not in
        // post-lookahead. If there is no lookahead happening (!isLookingAhead && !didLookahead),
        // update the anchors in the main pass.
        if (isLookingAhead || !didLookahead) {
            val width = placeable.width.toFloat()
            val newAnchors = DraggableAnchors {
                ExerciseAnchors.UNSELECTED at 0f
                ExerciseAnchors.SELECTED at -84f
            }
            state.anchoredDraggableState.updateAnchors(newAnchors)
        }
        didLookahead = isLookingAhead || didLookahead
        return layout(placeable.width, placeable.height) {
            // In a lookahead pass, we use the position of the current target as this is where any
            // ongoing animations would move. If SwipeToDismissBox is in a settled state, lookahead
            // and post-lookahead will converge.
            val xOffset = if (isLookingAhead) {
                state.anchoredDraggableState.anchors.positionOf(state.targetValue)
            } else state.requireOffset()
            placeable.place(xOffset.roundToInt(), 0)
        }
    }
}
