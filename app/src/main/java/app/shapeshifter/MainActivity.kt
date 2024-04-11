package app.shapeshifter

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import app.shapeshifter.feature.home.ui.exercisedetail.ExerciseDetailScreen
import app.shapeshifter.inject.ActivityComponent
import app.shapeshifter.inject.ApplicationComponent
import app.shapeshifter.inject.create
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val applicationComponent = ApplicationComponent.from(this)
        val component = ActivityComponent.create(this, applicationComponent)

        setContent {
            val backStack = rememberSaveableBackStack(root = ExerciseDetailScreen)
            val navigator = rememberCircuitNavigator(backStack)

            component.shapeShifterContent(
                backStack,
                navigator,
                Modifier,
            )
        }
    }
}

private fun ApplicationComponent.Companion.from(context: Context): ApplicationComponent {
    return (context.applicationContext as ShapeShifterApplication).component
}
