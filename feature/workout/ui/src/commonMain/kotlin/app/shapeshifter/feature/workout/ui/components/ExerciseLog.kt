package app.shapeshifter.feature.workout.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.shapeshifter.Clock
import app.shapeshifter.common.ui.compose.resources.Dimens

@Composable
fun ExerciseLog(
    name: String,
    onReorderExercises: () -> Unit,
    onRemoveExercise: () -> Unit,
    updateRestTime: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    val scope = rememberCoroutineScope()
    var showSetRestTimerBottomSheet by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.Padding.Medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.Padding.ExtraSmall),
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            val exerciseMenuItems = remember {
                listOf(
                    MenuItem(
                        id = 0,
                        name = "Reorder",
                        onClick = {
                            onReorderExercises()
                        },
                    ),
                    MenuItem(
                        id = 1,
                        name = "Remove Exercise",
                        onClick = {
                            onRemoveExercise()
                        },
                    ),
                )
            }
            IconButton(
                onClick = { showSetRestTimerBottomSheet = true },
            ) {
                Icon(
                    imageVector = Clock,
                    tint = Color.Unspecified,
                    contentDescription = "Rest Time Duration",
                    modifier = Modifier
                        .size(24.dp),
                )
            }

            if (showSetRestTimerBottomSheet) {
                SetRestTimerBottomSheet(
                    onDismiss = { showSetRestTimerBottomSheet = false },
                    onConfirm = updateRestTime,
                    modifier = Modifier,
                )
            }

            ThreeDotMenu(
                modifier = Modifier,
                menuItems = exerciseMenuItems,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.Padding.ExtraSmall),
        ) {
            val textFieldState = rememberTextFieldState()

            BasicTextField(
                state = textFieldState,
                textStyle = MaterialTheme.typography.bodySmall
                    .copy(
                        color = Color.Gray,
                    ),
                cursorBrush = SolidColor(Color.Gray),
                decorator = { innerTextField ->
                    if (textFieldState.text.isEmpty()) {
                        Text(
                            text = "Go slow and control each movement",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                        )
                    }

                    innerTextField()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.Padding.Medium),
            )
        }
    }


}

@Composable
fun SetColumnTitles(
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
        Box(
            modifier = Modifier
                .weight(1f),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetRestTimerBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
    modifier: Modifier,
) {
    val minutes = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val seconds = listOf(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55)

    var selectedMinutes by remember { mutableIntStateOf(2) }
    var selectedSeconds by remember { mutableIntStateOf(0) }

    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = {
            it != SheetValue.Hidden
        },
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Padding.ExtraSmall),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(Dimens.Padding.Small),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Set  Rest  Timer",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f),
                )

                IconButton(
                    onClick = onDismiss,
                    colors = IconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                NumberPicker(
                    onValueSelected = { value ->
                        selectedMinutes = value
                    },
                    initialValue = 2,
                    range = minutes,
                    modifier = Modifier
                        .padding(Dimens.Padding.Medium),

                )

                Text(
                    text = "Minutes",
                    fontWeight = FontWeight.Bold,
                )

                NumberPicker(
                    onValueSelected = { value ->
                        selectedSeconds = value
                    },
                    initialValue = 5,
                    range = seconds,
                    modifier = Modifier
                        .padding(Dimens.Padding.Medium),
                )

                Text(
                    text = "Seconds",
                    fontWeight = FontWeight.Bold,
                )
            }
            Button(
                onClick = {
                    onConfirm(selectedMinutes, selectedSeconds)
                    onDismiss()
                },
                modifier = Modifier
                    .padding(
                        top = Dimens.Padding.Medium,
                        bottom = Dimens.Padding.ExtraMedium,
                    ),
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = "Done",
                )
            }
        }
    }
}

@Composable
fun NumberPicker(
    initialValue: Int,
    onValueSelected: (Int) -> Unit,
    range: List<Int>,
    modifier: Modifier,
) {
    val lazyListState = rememberLazyListState(initialValue-1)
    val centerOffset = 100.dp

    LazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
        contentPadding = PaddingValues(vertical = centerOffset),
        flingBehavior = rememberSnapFlingBehavior(lazyListState),
    ) {
        items(range) { value ->
            Text(
                text = value.toString(),
                modifier = Modifier
                    .padding(Dimens.Padding.ExtraSmall),
                textAlign = TextAlign.Center,
                color = if (lazyListState.layoutInfo.visibleItemsInfo
                        .firstOrNull { it.index == range.indexOf(value) }?.offset == 0
                ) MaterialTheme.colorScheme.onSurface else Color.Gray,
            )
        }
    }
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex -1 }
            .collect { index ->
                val selectedValue = range.getOrNull(index)
                if (selectedValue != null) {
                    onValueSelected(selectedValue)
                }
            }
    }
}

val pattern = Regex("^\\d*\$")

