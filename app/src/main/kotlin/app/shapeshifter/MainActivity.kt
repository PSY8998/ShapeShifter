package app.shapeshifter

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.viewModelFactory
import app.shapeshifter.common.ui.compose.screens.HomeScreen
import app.shapeshifter.common.ui.compose.screens.OnboardingScreen
import app.shapeshifter.inject.ActivityComponent
import app.shapeshifter.inject.ApplicationComponent
import app.shapeshifter.inject.MainViewModel
import app.shapeshifter.inject.create
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdgeForTheme(true)
        super.onCreate(savedInstanceState)
        val applicationComponent = ApplicationComponent.from(this)
        val component = ActivityComponent.create(this, applicationComponent)
        val viewModel by viewModels<MainViewModel> {
            viewModelFactory { addInitializer(MainViewModel::class) { component.mainViewModelFactory() } }
        }

        val isOnboardingCompleted = runBlocking {
            viewModel.isOnboardingCompleted.firstOrNull() ?: false
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

private fun ApplicationComponent.Companion.from(context: Context): ApplicationComponent {
    return (context.applicationContext as ShapeShifterApplication).component
}
