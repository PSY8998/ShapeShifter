package app.shapeshifter.feature.exercise.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject
import kotlinx.parcelize.Parcelize

@Parcelize
data object ExerciseDetailScreen : Screen

@Inject
class ExerciseDetailUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is ExerciseDetailScreen -> ui<ExerciseDetailState> { _, _ ->
                ExerciseDetail()
            }

            else -> null
        }
    }
}

@Preview
@Composable
fun ExerciseDetail() {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Box(
            modifier = Modifier
                .systemBarsPadding(),
        ) {
            Text(text = "Exercise details")
        }
    }
}
