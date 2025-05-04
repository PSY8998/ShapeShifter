package app.shapeshifter.feature.workout.ui.createworkoutplan.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import com.aay.compose.radarChart.RadarChart
import com.aay.compose.radarChart.model.NetLinesStyle
import com.aay.compose.radarChart.model.Polygon
import com.aay.compose.radarChart.model.PolygonStyle

@Composable
fun ExerciseTypeRadarChart(
    labels: List<String> = listOf(
        "Strength",
        "Cardio",
        "Flexibility",
        "Endurance",
        "Core",
    ),
    values: List<Double> = listOf(90.0, 80.0, 65.0, 35.0, 20.0),
    modifier: Modifier = Modifier,
) {
    val labelsStyle = MaterialTheme.typography.bodyMedium
        .copy(color = MaterialTheme.colorScheme.onSurface)

    val scalarValuesStyle = MaterialTheme.typography.bodyMedium
        .copy(color = Color.Transparent)

    RadarChart(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth(align = Alignment.CenterHorizontally)
            .fillMaxWidth(fraction = 0.8f)
            .aspectRatio(1f),
        radarLabels = labels,
        labelsStyle = labelsStyle,
        netLinesStyle = NetLinesStyle(
            netLineColor = MaterialTheme.colorScheme.onSurface,
            netLinesStrokeWidth = 4f,
            netLinesStrokeCap = StrokeCap.Round,
        ),
        scalarSteps = 2,
        scalarValue = 100.0,
        scalarValuesStyle = scalarValuesStyle,
        polygons = listOf(
            Polygon(
                values = values,
                unit = "",
                style = PolygonStyle(
                    fillColor = MaterialTheme.colorScheme.onSurface,
                    fillColorAlpha = 0.5f,
                    borderColor = MaterialTheme.colorScheme.onSurface,
                    borderColorAlpha = 0.5f,
                    borderStrokeWidth = 2f,
                    borderStrokeCap = StrokeCap.Round,
                ),
            ),
        ),
    )
}
