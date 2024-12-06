package app.shapeshifter.feature.workout.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import app.shapeshifter.feature.workout.ui.trackworkout.TrackWorkoutUiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseLog(
    name: String,
    onReorderExercises: () -> Unit,
    onRemoveExercise: () -> Unit,
    updateRestTime: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    val sheetState = rememberModalBottomSheetState()
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
                    sheetState = sheetState,
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
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
    modifier: Modifier,
) {
    val minutes = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val seconds = listOf(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55)

    var selectedMinutes by remember { mutableIntStateOf(2) }
    var selectedSeconds by remember { mutableIntStateOf(0) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NumberPicker(
                onValueChange = { value ->
                    selectedMinutes = value
                },
                range = minutes,
                modifier = Modifier
                    .height(100.dp)
                    .padding(Dimens.Padding.Medium),
            )

            Text(
                text = "Minutes",
            )

            NumberPicker(
                onValueChange = { value ->
                    selectedSeconds = value
                },
                range = seconds,
                modifier = Modifier
                    .height(100.dp)
                    .padding(Dimens.Padding.Medium),
            )

            Text(
                text = "Seconds",
            )
        }
        Button(
            onClick = { onConfirm(selectedMinutes, selectedSeconds) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
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

@Composable
fun NumberPicker(
    onValueChange: (Int) -> Unit,
    range: List<Int>,
    modifier: Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        items(range) { value ->
            Text(
                text = value.toString(),
                modifier = Modifier
                    .clickable { onValueChange(value) },
            )
        }
    }
}

val pattern = Regex("^\\d*\$")

