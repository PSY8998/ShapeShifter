package app.shapeshifter

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import app.shapeshifter.common.ui.compose.screens.HomeScreen
import app.shapeshifter.common.ui.compose.screens.OnboardingScreen
import app.shapeshifter.feature.root.ui.RootViewModel
import app.shapeshifter.shared.prod.AndroidActivityComponent
import app.shapeshifter.shared.prod.AndroidApplicationComponent
import app.shapeshifter.shared.prod.create
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdgeForTheme(true)
        super.onCreate(savedInstanceState)
        val applicationComponent = AndroidApplicationComponent.from(this)
        val component = AndroidActivityComponent.create(this, applicationComponent)
        val rootViewModel: RootViewModel = applicationComponent.rootViewModel()

        val isOnboardingCompleted = runBlocking {
            rootViewModel.isOnboardingCompleted.firstOrNull() ?: false
        }

        setContent {
            val backStack = rememberSaveableBackStack(
                root = if (isOnboardingCompleted) HomeScreen else OnboardingScreen,
            )
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

private fun AndroidApplicationComponent.Companion.from(context: Context): AndroidApplicationComponent {
    return (context.applicationContext as ShapeShifterApplication).component
}
