package app.shapeshifter

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.shapeshifter.common.ui.compose.screens.HomeScreen
import app.shapeshifter.shared.prod.DesktopApplicationComponent
import app.shapeshifter.shared.prod.WindowComponent
import app.shapeshifter.shared.prod.create
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator

fun main() = application {
    val applicationComponent = remember {
        DesktopApplicationComponent.create()
    }

    Window(
        title = "ShapeShifter",
        onCloseRequest = ::exitApplication,
    ) {
        val component = remember(applicationComponent) {
            WindowComponent.create(applicationComponent)
        }

        val backstack = rememberSaveableBackStack(listOf(HomeScreen))
        val navigator = rememberCircuitNavigator(backstack) { /* no-op */ }

        component.shapeShifterContent(
            backstack,
            navigator,
            Modifier,
        )
    }
}
