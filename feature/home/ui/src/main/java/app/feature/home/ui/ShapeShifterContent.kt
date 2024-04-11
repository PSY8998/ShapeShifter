package app.feature.home.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.runtime.Navigator
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias ShapeShifterContent = @Composable (
    backstack: SaveableBackStack,
    navigator: Navigator,
    modifier: Modifier,
) -> Unit

@Inject
@Composable
fun ShapeShifterContent(
    @Assisted backstack: SaveableBackStack,
    @Assisted navigator: Navigator,
    circuit: Circuit,
    @Assisted modifier: Modifier,
) {
    CircuitCompositionLocals(circuit) {
        MaterialTheme {
            NavigableCircuitContent(
                navigator = navigator,
                backStack = backstack,
                modifier = modifier,
            )
        }
    }
}
