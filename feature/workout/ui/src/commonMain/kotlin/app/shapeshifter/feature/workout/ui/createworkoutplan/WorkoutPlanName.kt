package app.shapeshifter.feature.workout.ui.createworkoutplan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.resources.Dimens
import com.slack.circuit.overlay.OverlayHost
import com.slack.circuit.overlay.OverlayNavigator
import com.slack.circuitx.overlays.BottomSheetOverlay

sealed interface WorkoutPlanNameResult {

    data class AddExercisesToPlan(
        val name: String,
    ) : WorkoutPlanNameResult

    data object Dismiss : WorkoutPlanNameResult
}

@OptIn(ExperimentalMaterial3Api::class)
suspend fun OverlayHost.showWorkoutPlanName(): WorkoutPlanNameResult {
    return show(
        bottomSheetOverlay { navigator ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.Spacing.Medium),
            ) {
                Text(
                    text = "What do you want to name this plan?",
                )

                var planName by remember { mutableStateOf("") }

                TextField(
                    modifier = Modifier
                        .padding(top = Dimens.Spacing.Small)
                        .fillMaxWidth(),
                    value = planName,
                    onValueChange = {
                        planName = it
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.secondary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    shape = MaterialTheme.shapes.small,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Words,
                    ),
                    placeholder = {
                        Text(
                            text = "Plan name",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                        )
                    },
                )

                Button(
                    onClick = {
                        navigator.finish(WorkoutPlanNameResult.AddExercisesToPlan(planName))
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(
                            top = Dimens.Spacing.Medium,
                            bottom = Dimens.Spacing.Large,
                        ),
                    shape = MaterialTheme.shapes.small,
                    enabled = planName.isNotBlank(),
                ) {
                    Text(
                        text = "+ Add Exercises",
                    )
                }
            }
        },
    )
}


@ExperimentalMaterial3Api
private fun bottomSheetOverlay(
    content: @Composable (navigator: OverlayNavigator<WorkoutPlanNameResult>) -> Unit,
): BottomSheetOverlay<*, WorkoutPlanNameResult> {
    return BottomSheetOverlay(
        model = Unit,
        sheetShape = RoundedCornerShape(
            topEnd = 16.dp,
            topStart = 16.dp,
        ),
        onDismiss = { WorkoutPlanNameResult.Dismiss },
    ) { _, navigator ->
        Surface(
            modifier = Modifier
                .wrapContentSize(),
            shape = RoundedCornerShape(
                topEnd = 16.dp,
                topStart = 16.dp,
            ),
            color = MaterialTheme.colorScheme.surface,
        ) {
            content(navigator)
        }
    }
}
