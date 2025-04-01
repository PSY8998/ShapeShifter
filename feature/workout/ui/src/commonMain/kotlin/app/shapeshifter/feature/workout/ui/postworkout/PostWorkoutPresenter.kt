package app.shapeshifter.feature.workout.ui.postworkout

import androidx.compose.runtime.Composable
import app.shapeshifter.common.ui.compose.screens.PostWorkoutScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class PostWorkoutPresenterFactory(
    private val presenterFactory: (Navigator) -> PostWorkoutPresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when (screen) {
            is PostWorkoutScreen -> presenterFactory(navigator)
            else -> null
        }
    }

}

@Inject
class PostWorkoutPresenter(
    @Assisted private val navigator: Navigator,
) : Presenter<PostWorkoutUiState> {
    @Composable
    override fun present(): PostWorkoutUiState {

        fun eventSink(event: PostWorkoutEvent) {
            when (event) {

                PostWorkoutEvent.LogNotes -> TODO()
                PostWorkoutEvent.ReturnHome -> TODO()
                PostWorkoutEvent.ShareProgress -> TODO()
                PostWorkoutEvent.ViewHistory -> TODO()
            }
        }

        return PostWorkoutUiState(
            totalTime = "00:00",
            caloriesBurned = 440,
            exerciseCount = 5,
            setCount = 15,
            hasNewPR = true,
            // Add isLoading to state if you want to show a loading indicator in the UI
            // isLoading = isLoading,
            eventSink = ::eventSink,
        )
    }
}
