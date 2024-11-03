package app.shapeshifter.feature.workout.ui.components

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.resources.Dimens
import kotlin.math.roundToInt

@Composable
fun SetLog(
    index: Int,
    weight: Int,
    reps: Int,
    isBeingTracked: Boolean,
    isChecked: Boolean,
    onCheckChanged: (
        isChecked: Boolean,
        weight: Int,
        reps: Int,
    ) -> Unit,
    modifier: Modifier = Modifier,
    prevWeight: Int? = null,
    prevReps: Int? = null,
) {
    var isCompleted by remember(isChecked) {
        mutableStateOf(isChecked)
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
            .padding(vertical = Dimens.Padding.Small),
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

        if (isBeingTracked) {
            Text(
                text = "${prevWeight ?: 0}kg" + " x " + "${prevReps ?: 0}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier
                    .weight(1f),
            )
        }

        var setWeight: String by remember(weight) {
            mutableStateOf(weight.takeIf { it != 0 }?.toString() ?: "")
        }

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
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .wrapContentWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    if (setWeight.isBlank()) {
                        Text(
                            text = prevWeight?.toString() ?: "0",
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

        var setReps by remember(reps) {
            mutableStateOf(reps.takeIf { it != 0 }?.toString() ?: "")
        }

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
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .wrapContentWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    if (setReps.isBlank()) {
                        Text(
                            text = prevReps?.toString() ?: "0",
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
                        onCheckChanged(
                            it,
                            if (setWeight.isBlank()) prevWeight ?: 0 else setWeight.toIntOrNull()
                                ?: 0,
                            if (setReps.isBlank()) prevReps ?: 0 else setReps.toIntOrNull()
                                ?: 0,
                        )
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

enum class SetAnchors {
    SELECTED,
    UNSELECTED,
    OVERSCROLL,
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SetAnchorBox(
    backgroundContent: @Composable RowScope.(progress: Float) -> Unit,
    content: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val density = LocalDensity.current

    val state = rememberSaveable(
        saver = AnchoredDraggableState.Saver(
            snapAnimationSpec = spring(
                stiffness = Spring.StiffnessMedium,
                dampingRatio = Spring.DampingRatioMediumBouncy,
            ),
            decayAnimationSpec = exponentialDecay(),
            positionalThreshold = { totalDistance: Float -> totalDistance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
        ),
    ) {
        AnchoredDraggableState(
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
    }
    Box(
        modifier = modifier,
        propagateMinConstraints = true,
    ) {
        val selectedProgress = state.progress(SetAnchors.UNSELECTED, SetAnchors.SELECTED)
        Row(
            content = {
                backgroundContent(selectedProgress)
            },
            modifier = Modifier.matchParentSize(),
        )
        Row(
            content = content,
            modifier = Modifier
                .anchoredDraggable(
                    state = state,
                    orientation = Orientation.Horizontal,
                    enabled = enabled,
                )
                .offset {
                    IntOffset(
                        x = state
                            .requireOffset()
                            .roundToInt(),
                        y = 0,
                    )
                },
        )
    }
}
