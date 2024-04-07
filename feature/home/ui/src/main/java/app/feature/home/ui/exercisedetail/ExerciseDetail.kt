package app.feature.home.ui.exercisedetail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import app.feature.home.ui.EmptyUiState
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import kotlinx.parcelize.Parcelize
import me.tatarka.inject.annotations.Inject

@Parcelize
data object ExerciseDetailScreen : Screen

@Inject
class ExerciseDetailUiFactory: Ui.Factory{
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when(screen){
            is ExerciseDetailScreen -> ui<EmptyUiState> { state, modifier ->
                ExerciseDetail()
            }
            else -> null
        }
    }

}

@Composable
fun ExerciseDetail() {
    Text(text = "Exercise details")
}