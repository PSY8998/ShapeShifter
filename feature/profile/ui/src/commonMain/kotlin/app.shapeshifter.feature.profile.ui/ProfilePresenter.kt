package app.shapeshifter.feature.profile.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.shapeshifter.common.ui.compose.screens.HomeScreen
import app.shapeshifter.common.ui.compose.screens.ProfileScreen
import app.shapeshifter.feature.workout.domain.ObserveWorkoutSessionsUseCase
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ProfilePresenterFactory(
    private val presenterFactory: (ProfileScreen, Navigator) -> ProfilePresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when (screen) {
            is ProfileScreen -> presenterFactory(screen, navigator)
            else -> null
        }
    }

}

@Inject
class ProfilePresenter(
    @Assisted private val screen: ProfileScreen,
    @Assisted private val navigator: Navigator,
    private val observeWorkoutSessionsUseCase: ObserveWorkoutSessionsUseCase,
) : Presenter<ProfileUiState> {
    @Composable
    override fun present(): ProfileUiState {
        val workoutSessions by observeWorkoutSessionsUseCase.flow.collectAsState(emptyList())

        fun eventSink(event: ProfileUiEvent){
            when(event){
                is ProfileUiEvent.GoBack -> navigator.resetRoot(
                    newRoot = HomeScreen,
                    saveState = true,
                    restoreState = true,
                )
            }
        }

        LaunchedEffect(Unit) {
            observeWorkoutSessionsUseCase(Unit)
        }
        return ProfileUiState(
            workouts = workoutSessions,
            eventSink = ::eventSink
        )
    }
}
