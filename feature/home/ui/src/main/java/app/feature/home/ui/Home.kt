package app.feature.home.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import kotlinx.parcelize.Parcelize
import me.tatarka.inject.annotations.Inject

@Parcelize
data object HomeScreen : Screen

@Inject
class HomeUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is HomeScreen -> {
                ui<EmptyUiState> { _, modifier ->
                    Home(
                        modifier = modifier
                    )
                }
            }

            else -> null
        }
    }
}


@Composable
fun Home(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Text(
            text = "ShapeShifter",
            modifier = Modifier
                .padding(paddingValues)
        )
    }
}
