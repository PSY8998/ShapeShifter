package app.shapeshifter

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import app.shapeshifter.common.ui.compose.screens.ExerciseDetailScreen
import app.shapeshifter.common.ui.compose.screens.HomeScreen
import app.shapeshifter.inject.ActivityComponent
import app.shapeshifter.inject.ApplicationComponent
import app.shapeshifter.inject.create
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdgeForTheme(false)
        super.onCreate(savedInstanceState)
        val applicationComponent = ApplicationComponent.from(this)
        val component = ActivityComponent.create(this, applicationComponent)

        setContent {
            val backStack = rememberSaveableBackStack(root = HomeScreen)
            val navigator = rememberCircuitNavigator(backStack)

            component.shapeShifterContent(
                backStack,
                navigator,
                Modifier,
            )
        }
    }
}

private fun ComponentActivity.enableEdgeToEdgeForTheme(useDarkTheme: Boolean) {
    val style = if (useDarkTheme) {
        SystemBarStyle.dark(Color.TRANSPARENT)
    } else {
        SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
    }
    enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
}

private fun ApplicationComponent.Companion.from(context: Context): ApplicationComponent {
    return (context.applicationContext as ShapeShifterApplication).component
}
