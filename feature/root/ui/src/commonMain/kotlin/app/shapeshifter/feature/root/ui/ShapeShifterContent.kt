package app.shapeshifter.feature.root.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.shapeshifter.common.ui.compose.theme.ShapeShifterTheme
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
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

@OptIn(ExperimentalCoilApi::class)
@Inject
@Composable
fun ShapeShifterContent(
    @Assisted backStack: SaveableBackStack,
    @Assisted navigator: Navigator,
    circuit: Circuit,
    imageLoader: ImageLoader,
    @Assisted modifier: Modifier,
) {
    setSingletonImageLoaderFactory { imageLoader }

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
