package app.shapeshifter.feature.root.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.shapeshifter.common.ui.compose.theme.ShapeShifterTheme
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.runtime.Navigator
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias ShapeShifterContent = @Composable (
    backStack: SaveableBackStack,
    navigator: Navigator,
    modifier: Modifier,
) -> Unit

@Inject
@Composable
fun ShapeShifterContent(
    @Assisted backStack: SaveableBackStack,
    @Assisted navigator: Navigator,
    circuit: Circuit,
    @Assisted modifier: Modifier,
) {
    CircuitCompositionLocals(circuit) {
        ShapeShifterTheme(
            useDarkTheme = false,
        ) {
            Root(
                navigator = navigator,
                backStack = backStack,
                modifier = modifier,
            )
        }
    }
}
