package app.shapeshifter.feature.workout.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.shapeshifter.common.ui.compose.resources.Dimens
import app.shapeshifter.feature.workout.ui.createworkoutplan.bottomSheetOverlay
import com.slack.circuit.overlay.OverlayHost

sealed interface SelectedRestTimeResult {

    data class RestTimeSelected(
        val minutes: Int.Companion,
        val seconds: Int.Companion,
    ) : SelectedRestTimeResult

    data object Dismiss : SelectedRestTimeResult
}

@OptIn(ExperimentalMaterial3Api::class)
suspend fun OverlayHost.showRestTimeSelector(): SelectedRestTimeResult {
    return show(
        bottomSheetOverlay<SelectedRestTimeResult>(
            onDismiss = {
                SelectedRestTimeResult.Dismiss
            },
        ) { navigator ->

            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                var minutes by remember { mutableStateOf(Int) }
                var seconds by remember { mutableStateOf(Int) }

                Row(
                    modifier = Modifier,
                ) {

                    val state = rememberLazyListState()
                    val density = LocalDensity.current

                    val snappingLayout =
                        remember(state, density) {
                            val snapPosition =
                                object : SnapPosition {
                                    override fun position(
                                        layoutSize: Int,
                                        itemSize: Int,
                                        beforeContentPadding: Int,
                                        afterContentPadding: Int,
                                        itemIndex: Int,
                                        itemCount: Int,
                                    ): Int {
                                        return with(density) { beforeContentPadding + 20.dp.roundToPx() }
                                    }
                                }
                            SnapLayoutInfoProvider(state, snapPosition)
                        }
                    val flingBehavior = rememberSnapFlingBehavior(snappingLayout)

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        state = state,
                        flingBehavior = flingBehavior,
                    ) {
                        items(10) {
                            Box(
                                modifier = Modifier
                                    .height(50.dp)
                                    .width(25.dp)
                                    .padding(Dimens.Padding.ExtraSmall)
                                    .background(MaterialTheme.colorScheme.background),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(it.toString(), fontSize = 8.sp)
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        navigator.finish(
                            SelectedRestTimeResult.RestTimeSelected(
                                minutes = minutes,
                                seconds = seconds,
                            ),
                        )
                    },
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
        },
    )
}
