package app.shapeshifter.feature.exercise.ui.exercises

import androidx.compose.runtime.Composable
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Inject

@Inject
class ExercisesPresenterFactory(
    private val exercisesPresenter: ExercisesPresenter
): Presenter.Factory {

    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when(screen){
            is ExercisesScreen -> {
                exercisesPresenter
            }
            else -> null
        }
    }

}

@Inject
class ExercisesPresenter(): Presenter<ExercisesState>{

    @Composable
    override fun present(): ExercisesState {
        return ExercisesState
    }

}
