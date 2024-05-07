package app.shapeshifter.feature.exercise.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.screens.ExerciseDetailScreen
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import shapeshifter.feature.exercise.ui.generated.resources.Res
import shapeshifter.feature.exercise.ui.generated.resources.exercise_deadlift
import shapeshifter.feature.exercise.ui.generated.resources.ic_dumbbell

@Inject
class ExerciseDetailUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is ExerciseDetailScreen -> ui<ExerciseDetailState> { state, _ ->
                ExerciseDetail(state)
            }

            else -> null
        }
    }
}

@Composable
internal fun ExerciseDetail(
    state: ExerciseDetailState,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.navigationBars,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            HeaderComponent(
                contentPadding = WindowInsets.statusBars.asPaddingValues(),
                onBack = {
                    state.eventSink(ExerciseDetailUiEvent.GoBack)
                },
                modifier = Modifier
                    .fillMaxWidth(),
            )

            var text by rememberRetained(key = "exerciseName") { mutableStateOf("") }

            LazyColumn(
                modifier = Modifier
                    .imePadding()
                    .padding(top = 16.dp)
                    .weight(1f),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                ),
            ) {
                item(
                    key = "exercise_name",
                ) {
                    ExerciseNameTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        exerciseName = text,
                        onExerciseNameChanged = {
                            text = it
                        },
                    )
                }

                item {
                    Spacer(
                        modifier = Modifier
                            .height(16.dp),
                    )
                }
            }

            CreateExerciseButton(
                onCreate = {
                    state.eventSink(
                        ExerciseDetailUiEvent.CreateExercise(exerciseName = text),
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun HeaderComponent(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Box(
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(Res.drawable.exercise_deadlift),
            contentDescription = "exercise image",
            modifier = Modifier
                .fillMaxWidth(),
        )
        Row(
            modifier = Modifier
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            IconButton(
                onClick = {
                    onBack()
                },
                modifier = Modifier,

            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    contentDescription = "",
                    modifier = Modifier
                        .size(32.dp),
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }

            val iconSize = with(LocalDensity.current) {
                48.dp.toPx().toInt()
            }

            Text(
                text = "Exercise details",
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                    .offset { IntOffset(x = -iconSize, y = 0) },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ExerciseNameTextField(
    exerciseName: String,
    onExerciseNameChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Name of Exercise",
            modifier = Modifier,
        )

        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.LightGray, shape = CircleShape)
                .padding(16.dp),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_dumbbell),
                        contentDescription = "Exercise name leading icon",
                    )
                    Box(
                        modifier = Modifier
                            .height(16.dp)
                            .width(1.dp)
                            .background(color = Color.Gray),
                    )
                    innerTextField()
                }
            },
            value = exerciseName,
            onValueChange = {
                onExerciseNameChanged(it)
            },
            textStyle = MaterialTheme.typography.bodyMedium.merge(
                fontWeight = FontWeight.Bold,
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
            ),
        )
    }
}

@Composable
private fun CreateExerciseButton(
    modifier: Modifier = Modifier,
    onCreate: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = {
                onCreate()
            },
            modifier = Modifier,

        ) {
            Text("Create Exercise")
        }
    }
}
