package app.shapeshifter.feature.workout.ui.exercisesequence

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.NestedScaffold
import app.shapeshifter.common.ui.compose.resources.Dimens
import app.shapeshifter.common.ui.compose.screens.ExerciseSequenceScreen
import app.shapeshifter.data.models.workout.ExerciseLogSession
import app.shapeshifter.feature.workout.ui.components.DraggableItem
import app.shapeshifter.feature.workout.ui.components.dragContainer
import app.shapeshifter.feature.workout.ui.components.rememberDragDropState
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject

@Inject
class ExerciseSequenceUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is ExerciseSequenceScreen -> ui<ExerciseSequenceUiState> { state, modifier ->
                ReorderExercises(
                    uiState = state,
                    modifier = modifier,
                )
            }

            else -> null
        }
    }
}

@Composable
internal fun ReorderExercises(
    uiState: ExerciseSequenceUiState,
    modifier: Modifier = Modifier,
) {
    NestedScaffold(
        modifier = modifier
            .fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when (uiState) {
                is ExerciseSequenceUiState.Empty -> {}
                is ExerciseSequenceUiState.Filled -> {
                    ReorderableExerciseList(
                        exerciseSessions = uiState.exerciseSessions,
                        modifier = Modifier,
                        onReorderedExercises = {
                            uiState.eventSink(
                                ExerciseSequenceUiEvent.OnReorderedExercises(
                                    exerciseSessions = it,
                                ),
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun ReorderableExerciseList(
    exerciseSessions: List<ExerciseLogSession>,
    onReorderedExercises: (List<ExerciseLogSession>) -> Unit,
    modifier: Modifier,
) {
    var list by remember { mutableStateOf(exerciseSessions) }

    val listState = rememberLazyListState()
    val dragDropState = rememberDragDropState(listState) { fromIndex, toIndex ->
        list = list.toMutableList().apply { add(toIndex, removeAt(fromIndex)) }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .dragContainer(dragDropState),
            state = listState,
            contentPadding = PaddingValues(Dimens.Padding.Small),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            itemsIndexed(list, key = { _, item -> item.exercise.id }) { index, item ->
                DraggableItem(dragDropState, index) { isDragging ->
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(if (isDragging) 8.dp else 1.dp),
                        colors = CardDefaults.elevatedCardColors(
                            if (isDragging) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSecondaryContainer,
                        ),
                    ) {
                        Text(
                            text = item.exerciseTemplate.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                        )
                    }
                }
            }
        }

        Button(
            onClick = { onReorderedExercises(list) },
            modifier = Modifier,
        ) {
            Text("Save")
        }
    }
}

