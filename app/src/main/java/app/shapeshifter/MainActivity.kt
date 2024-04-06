package app.shapeshifter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import app.feature.home.ui.Home
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.circuit
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import kotlinx.parcelize.Parcelize

class MainActivity : ComponentActivity() {

    val circuit = Circuit.Builder()
        .addUiFactory(HomeUiFactory())
        .addPresenterFactory(HomePresenterFactory())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CircuitCompositionLocals(circuit = circuit) {
                val backStack = rememberSaveableBackStack(root = HomeScreen)
                val navigator = rememberCircuitNavigator(backStack)
                NavigableCircuitContent(navigator, backStack)
            }
        }
    }
}

class HomeUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is HomeScreen -> {
                ui<EmptyUiState> { state, modifier ->
                    Home()
                }
            }

            else -> null
        }
    }
}

class HomePresenterFactory : Presenter.Factory{
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext
    ): Presenter<*>? {
        return when(screen){
            is HomeScreen -> HomePresenter()
            else -> null
        }
    }

}
class HomePresenter : Presenter<EmptyUiState> {
    @Composable
    override fun present(): EmptyUiState {
        return EmptyUiState()
    }
}

@Parcelize
data object HomeScreen : Screen

class EmptyUiState : CircuitUiState