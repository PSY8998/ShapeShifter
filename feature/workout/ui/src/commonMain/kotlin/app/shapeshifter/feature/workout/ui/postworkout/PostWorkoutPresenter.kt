package app.shapeshifter.feature.workout.ui.postworkout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.shapeshifter.common.ui.compose.screens.PostWorkoutScreen
import app.shapeshifter.feature.workout.domain.ObserveWorkoutDetailsUseCase
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds


@Inject
class PostWorkoutPresenterFactory(
    private val presenterFactory: (Navigator, PostWorkoutScreen) -> PostWorkoutPresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when (screen) {
            is PostWorkoutScreen -> presenterFactory(navigator, screen)
            else -> null
        }
    }
}

@Inject
class PostWorkoutPresenter(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: PostWorkoutScreen,
    private val observeWorkoutDetailsUseCase: ObserveWorkoutDetailsUseCase,
) : Presenter<PostWorkoutUiState> {

    @Composable
    override fun present(): PostWorkoutUiState {
        // Collect the list of all sessions
        // Note: Assuming observeWorkoutSessionsUseCase provides a Flow. If it's a suspend fun, adjust accordingly.
        val session by observeWorkoutDetailsUseCase.flow.collectAsState(initial = null)

        LaunchedEffect(screen.workoutLogId) {
            observeWorkoutDetailsUseCase(ObserveWorkoutDetailsUseCase.Params(screen.workoutLogId))
        }

        // Derive and format the duration for the specific session
        val formattedTime = remember(session, screen.workoutLogId) { // Assuming screen has workoutId
            val duration = session?.let { workoutSession ->
                // Calculate duration - Assuming WorkoutSession has startTime and endTime as Long milliseconds
                // Adjust property names (e.g., startTimeInMillis, endTimeInMillis) if different in your model
                val startMillis = workoutSession.workoutLog.startTimeInMillis ?: return@let Duration.ZERO // Use actual property name
                val endMillis = workoutSession.workoutLog.finishTimeInMillis ?: return@let Duration.ZERO     // Use actual property name

                if (endMillis > startMillis) {
                    // Calculate difference and convert to Duration
                    (endMillis - startMillis).milliseconds
                } else {
                    Duration.ZERO
                }
            } ?: Duration.ZERO // Default to zero if session not found or times invalid

            formatDuration(duration) // Format the calculated duration (which is now guaranteed to be Duration)
        }



        fun eventSink(event: PostWorkoutEvent) {
            when (event) {
                PostWorkoutEvent.LogNotes -> TODO("Implement Log Notes Navigation/Action")
                PostWorkoutEvent.ShareProgress -> TODO("Implement Share Progress Action")
                // ReturnHome and ViewHistory removed as per request
            }
        }

        // Calculate state details
        val completedExercises = remember(session) {
            session?.exerciseSessions?.map { exerciseSession ->
                // Assuming ExerciseAndSets has an 'exercise' property of type ExerciseEntity
                // And ExerciseEntity has 'id' and 'name'
                exerciseSession.exercise.let { exercise ->
                    ExerciseInfo(id = exercise.id, name = exercise.name)
                }
            } ?: emptyList()
        }

        val setCount = remember(session) {
            session?.exerciseSessions?.sumOf { it.sets.size } ?: 0
        }

        // TODO: Implement actual record detection logic based on session data vs historical data
        val achievedRecords = remember(session) {
            if (completedExercises.isNotEmpty() && (session?.exerciseSessions?.flatMap { it.sets }?.size ?: 0) > 5) { // Placeholder condition
                 listOf(RecordInfo(name = "Heaviest Deadlift", value = "200 lb")) // Placeholder data
            } else {
                emptyList()
            }
        }


        // Return state with updated fields
        return PostWorkoutUiState(
            totalTime = formattedTime,
            caloriesBurned = 440, // TODO: Implement calorie calculation if needed
            setCount = setCount,
            completedExercises = completedExercises,
            achievedRecords = achievedRecords,
            eventSink = ::eventSink,
        )
    }

    // Helper function to format Duration
    private fun formatDuration(duration: Duration): String {
        if (duration.isNegative()) return "00:00"
        return duration.toComponents { hours, minutes, seconds, _ ->
            if (hours > 0) {
                "%02d:%02d:%02d".format(hours, minutes, seconds)
            } else {
                "%02d:%02d".format(minutes, seconds)
            }
        }
    }
}
