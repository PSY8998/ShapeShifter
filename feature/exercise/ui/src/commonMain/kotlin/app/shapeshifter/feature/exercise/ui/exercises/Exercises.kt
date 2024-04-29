@file:Suppress("ktlint:filename")

package app.shapeshifter.feature.exercise.ui.exercises

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.NestedScaffold
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import app.shapeshifter.data.models.Exercise
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import shapeshifter.feature.exercise.ui.generated.resources.Res
import shapeshifter.feature.exercise.ui.generated.resources.barbell_overhead
import shapeshifter.feature.exercise.ui.generated.resources.exercise_deadlift

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

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ExerciseScrollContent(
    modifier: Modifier = Modifier,
    exercises: List<Exercise>,
) {
    var selectedExerciseIds by remember { mutableStateOf(emptySet<Long>()) }

    LazyColumn(
        modifier = modifier,
    ) {
        items(exercises) { exercise ->
            val exerciseSelected by remember {
                derivedStateOf {
                    selectedExerciseIds.contains(exercise.id)
                }
            }

            val exerciseSelectedIndex by remember {
                derivedStateOf { if (exerciseSelected) selectedExerciseIds.indexOf(exercise.id) else -1 }
            }

            val textStyle = MaterialTheme.typography.labelLarge.merge(
                color = Color.White,
            )
            val textMeasurer = rememberTextMeasurer()

            val measure = remember(exerciseSelectedIndex) {
                textMeasurer.measure(text = exerciseSelectedIndex.toString(), style = textStyle)
            }

            val offset by animateIntAsState(
                if (exerciseSelected) measure.size.width + 64 else 0,
                label = "",
            )

            val cardShifting by remember { derivedStateOf { offset != 0 } }


            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp,
                ),
                modifier = Modifier
                    .pointerInput(exerciseSelected) {
                        detectTapGestures(
                            onTap = {
                                selectedExerciseIds = if (exerciseSelected) {
                                    selectedExerciseIds.minus(element = exercise.id)
                                } else {
                                    selectedExerciseIds.plus(element = exercise.id)
                                }
                            },
                        )
                    }
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(8.dp)
                    .drawBehind {
                        if (cardShifting) {
                            drawRoundRect(
                                color = Color.Black,
                                cornerRadius = CornerRadius(x = 12.dp.toPx(), y = 12.dp.toPx()),
                            )

                            drawText(
                                textLayoutResult = measure,
                                color = Color.White,
                                topLeft = Offset(
                                    x = size.width - measure.size.width - 32,
                                    y = size.height / 2 - measure.size.height / 2,
                                ),
                                alpha = 1f,
                            )
                        }
                    }
                    .offset {
                        IntOffset(x = -offset.toInt(), 0)
                    },
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(Res.drawable.exercise_deadlift),
                        contentDescription = "exercise image",
                        modifier = Modifier,
                    )

                    Column(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .align(Alignment.Top),
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 8.dp),
                            text = exercise.name,
                        )

                        Text(
                            modifier = Modifier
                                .padding(horizontal = 8.dp),
                            text = exercise.primaryMuscle.displayName,
                            color = Color.Gray,
                            style = MaterialTheme.typography.labelMedium,
                        )

                        Text(
                            modifier = Modifier
                                .padding(horizontal = 8.dp),
                            text = exercise.secondaryMuscle.joinToString("/ ") {
                                it.displayName
                            },
                            color = Color.Gray,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            }
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
