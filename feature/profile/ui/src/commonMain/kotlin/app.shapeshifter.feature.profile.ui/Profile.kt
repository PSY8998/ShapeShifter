package app.shapeshifter.feature.profile.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.shapeshifter.common.ui.compose.screens.ProfileScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject

@Inject
class ProfileUiFactory: Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when(screen) {
            is ProfileScreen -> {
                ui<EmptyUiState>{ _, modifier ->
                    Profile(modifier)
                }
            }
            else -> null
        }
    }
}

@Composable
fun Profile(
    modifier: Modifier = Modifier,
){

}
