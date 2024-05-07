package app.shapeshifter.feature.workout.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.shapeshifter.common.ui.compose.resources.Dimens
import com.slack.circuit.overlay.OverlayHost
import com.slack.circuit.overlay.OverlayNavigator
import com.slack.circuitx.overlays.BasicDialogOverlay
import com.slack.circuitx.overlays.DialogResult


/** A hypothetical confirmation dialog. */
@OptIn(ExperimentalMaterial3Api::class)
suspend fun OverlayHost.showDiscardWorkoutDialog(): DialogResult {
    return show(
        dialogOverlay { navigator ->
            Column(
                modifier = Modifier
                    .padding(Dimens.Spacing.Medium)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "Discard Workout?",
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    text = "Are you sure you want to discard this workout?",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(top = Dimens.Spacing.Small),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.Spacing.Medium),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = Dimens.Spacing.Medium,
                        alignment = Alignment.End,
                    ),
                ) {
                    TextButton(
                        onClick = {
                            navigator.finish(DialogResult.Dismiss)
                        },
                        modifier = Modifier,
                        shape = MaterialTheme.shapes.small,
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            navigator.finish(DialogResult.Confirm)
                        },
                        modifier = Modifier,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.errorContainer,
                        ),
                        shape = MaterialTheme.shapes.small,
                    ) {
                        Text("Discard")
                    }
                }
            }
        },
    )
}

@ExperimentalMaterial3Api
private fun dialogOverlay(
    properties: DialogProperties = DialogProperties(),
    content: @Composable (navigator: OverlayNavigator<DialogResult>) -> Unit,
): BasicDialogOverlay<*, DialogResult> {
    return BasicDialogOverlay(
        model = Unit,
        onDismissRequest = { DialogResult.Dismiss },
        properties = properties,
    ) { _, navigator ->
        Dialog(
            onDismissRequest = { navigator.finish(DialogResult.Dismiss) },
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentSize(),
                shape = MaterialTheme.shapes.small,
            ) {
                content(navigator)
            }
        }
    }
}
