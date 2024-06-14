package app.shapeshifter.feature.workout.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.resources.Dimens
import app.shapeshifter.data.models.PositiveInt
import app.shapeshifter.data.models.workoutlog.SetLog
import kotlin.math.roundToInt

@Composable
fun SetLog(
    setLog: SetLog,
    index: Int,
    onComplete: (
        set: SetLog,
    ) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isCompleted by remember(setLog.finishTime) {
        mutableStateOf(setLog.finishTime > 0)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isCompleted) {
                    Modifier.background(MaterialTheme.colorScheme.secondary)
                } else
                    Modifier,
            )
            .padding(vertical = Dimens.Spacing.Small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = (index + 1).toString(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f),
        )
        Text(
            text = "${setLog.prevWeight}" + "x" + "${setLog.prevReps}",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f),

            )

        var setWeight by remember { mutableStateOf(setLog.weight.toString()) }

        BasicTextField(
            value = setWeight,
            onValueChange = {
                if (pattern.matches(it)) {
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
            modifier = Modifier
                .wrapContentWidth(Alignment.CenterHorizontally)
                .weight(1f)
                .width(IntrinsicSize.Min)
                .defaultMinSize(24.dp),
        )

        var setReps by remember { mutableStateOf(setLog.reps.toString()) }

        BasicTextField(
            value = setReps,
            onValueChange = {
                if (pattern.matches(it)) {
                    setReps = it
                }
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Black,
            ),
            decorationBox = { internalField ->
                Box(
                    modifier = Modifier
                        .wrapContentWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    internalField()

                    if (setReps.isBlank()) {
                        Text(
                            text = "0",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Black,
                            ),
                        )
                    }
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
                        onComplete(
                            setLog.copy(
                                weight = PositiveInt(setWeight.toIntOrNull() ?: 0),
                                reps = PositiveInt(setReps.toIntOrNull() ?: 0),
                                finishTime = if (it) System.currentTimeMillis() else 0,
                            ),
                        )
                    },
                )
                .padding(Dimens.Spacing.ExtraSmall),
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

enum class SetAnchors {
    SELECTED,
    UNSELECTED,
}

@Composable
fun rememberSetAnchorState(
    initialValue: SetAnchors = SetAnchors.UNSELECTED,
    positionalThreshold: (totalDistance: Float) -> Float = { 42f },
    confirmValueChange: (SetAnchors) -> Boolean = { true },
): SetAnchorState {
    val density = LocalDensity.current
    return rememberSaveable(
        saver = SetAnchorState.Saver(
            density = density,
            positionalThreshold = positionalThreshold,
            confirmValueChange = confirmValueChange,
        ),
    ) {
        SetAnchorState(initialValue, density, positionalThreshold, confirmValueChange)
    }
}

@OptIn(ExperimentalFoundationApi::class)
class SetAnchorState(
    initialValue: SetAnchors,
    density: Density,
    positionalThreshold: (totalDistance: Float) -> Float,
    confirmValueChange: (SetAnchors) -> Boolean,
) {
    internal val anchoredDraggableState = AnchoredDraggableState(
        initialValue = initialValue,
        animationSpec = tween(),
        confirmValueChange = confirmValueChange,
        positionalThreshold = positionalThreshold,
        velocityThreshold = { with(density) { 148.dp.toPx() } },
    )

    val currentValue: SetAnchors get() = anchoredDraggableState.currentValue

    val targetValue: SetAnchors get() = anchoredDraggableState.targetValue

    internal val offset: Float get() = anchoredDraggableState.offset

    fun requireOffset(): Float = anchoredDraggableState.requireOffset()

    suspend fun dismiss(direction: SetAnchors) {
        anchoredDraggableState.animateTo(targetValue = direction)
    }

    companion object {

        /**
         * The default [Saver] implementation for [SwipeToDismissBoxState].
         */
        fun Saver(
            positionalThreshold: (totalDistance: Float) -> Float,
            confirmValueChange: (SetAnchors) -> Boolean,
            density: Density,
        ) = Saver<SetAnchorState, SetAnchors>(
            save = { it.currentValue },
            restore = {
                SetAnchorState(
                    initialValue = it,
                    density = density,
                    positionalThreshold = positionalThreshold,
                    confirmValueChange = confirmValueChange,
                )
            },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SetAnchorBox(
    state: SetAnchorState,
    backgroundContent: @Composable RowScope.() -> Unit,
    content: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    Box(
        modifier
            .anchoredDraggable(
                state = state.anchoredDraggableState,
                orientation = Orientation.Horizontal,
                enabled = enabled,
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
                .swipeToSetAnchors(state),
        )
    }
}

private fun Modifier.swipeToSetAnchors(
    state: SetAnchorState,
) = this then SetAnchorsElement(
    state,
)

private class SetAnchorsElement(
    private val state: SetAnchorState,
) : ModifierNodeElement<SetAnchorsNode>() {

    override fun create() = SetAnchorsNode(
        state,
    )

    override fun update(node: SetAnchorsNode) {
        node.state = state
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        other as SetAnchorsElement
        return state == other.state
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

private class SetAnchorsNode(
    var state: SetAnchorState,
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
                SetAnchors.UNSELECTED at 0f
                SetAnchors.SELECTED at -80.dp.toPx()
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
            } else {
                state.requireOffset()
            }
            placeable.place(xOffset.roundToInt(), 0)
        }
    }
}
