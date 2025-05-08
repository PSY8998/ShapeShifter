package app.shapeshifter.feature.workout.ui.createworkoutplan

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import app.shapeshifter.common.ui.compose.screens.CreateWorkoutPlanScreen
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import app.shapeshifter.data.models.PositiveInt
import app.shapeshifter.data.models.plans.SetPlan
import app.shapeshifter.data.models.plans.WorkoutPlanSession
import app.shapeshifter.feature.workout.domain.FetchExercisesUseCase
import app.shapeshifter.feature.workout.domain.SaveWorkoutUseCase
import app.shapeshifter.feature.workout.domain.SelectWorkoutPlanUseCase
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Inject
class CreateWorkoutPlanPresenterFactory(
    private val provideFactory: (Navigator, CreateWorkoutPlanScreen) -> CreateWorkoutPlanPresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when (screen) {
            is CreateWorkoutPlanScreen -> provideFactory(navigator, screen)
            else -> null
        }
    }
}

@Inject
class CreateWorkoutPlanPresenter(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: CreateWorkoutPlanScreen,
    private val workoutPlanSessionManagerProvider: Lazy<WorkoutPlanSessionManager>,
    private val fetchExercisesUseCase: FetchExercisesUseCase,
    private val saveWorkoutUseCase: SaveWorkoutUseCase,
    private val selectWorkoutPlanUseCase: SelectWorkoutPlanUseCase,
) : Presenter<CreateWorkoutPlanUiState> {

    @Composable
    override fun present(): CreateWorkoutPlanUiState {

        val scope = rememberCoroutineScope()

        val workoutPlanSessionManager = rememberRetained { workoutPlanSessionManagerProvider.value }

        var workoutPlanSession by rememberRetained { mutableStateOf<WorkoutPlanSession?>(null) }

        LaunchedEffect(workoutPlanSessionManager.currentPlan) {
            workoutPlanSessionManager.currentPlan.collectLatest { session ->
                workoutPlanSession = session
            }
        }

        LaunchedEffect(Unit) {
            when (val intent = screen.intent) {
                is CreateWorkoutPlanScreen.Intent.NewWorkoutPlan -> {
                    val session = workoutPlanSession
                    if (session != null && session.workoutPlan.id != 0L) {
                        workoutPlanSessionManager.loadExistingPlan(
                            existingPlan = session,
                        )
                    } else {
                        val managerCurrentPlan = workoutPlanSessionManager.currentPlan.value
                        if (managerCurrentPlan != null &&
                            managerCurrentPlan.workoutPlan.id == 0L
                        ) {
                            // Already working on a new plan in the manager, let it be.
                        } else {
                            workoutPlanSessionManager.createNewPlan(
                                routineId = intent.routineId,
                            )
                        }
                    }
                }

                is CreateWorkoutPlanScreen.Intent.EditWorkoutPlan -> {
                    val session = selectWorkoutPlanUseCase(
                        params = SelectWorkoutPlanUseCase.Params(intent.workoutPlanId),
                    ).getOrNull() ?: return@LaunchedEffect
                    workoutPlanSessionManager.loadExistingPlan(
                        existingPlan = session,
                    )
                }
            }
        }

        val answeringNavigator =
            rememberAnsweringNavigator<ExercisesScreen.Result.SelectedExercises>(navigator) { result ->
                val selectedExerciseIds = result.exerciseIds
                val exercises =
                    fetchExercisesUseCase(selectedExerciseIds).getOrNull() ?: emptyList()
                workoutPlanSessionManager.addExercises(
                    exercises = exercises,
                    startIndex = workoutPlanSessionManager.currentPlan.value?.exercisePlanSessions?.size
                        ?: 0,
                )
            }

        fun eventSink(event: CreateWorkoutPlanUiEvent) {
            when (event) {
                is CreateWorkoutPlanUiEvent.OnAddExercise -> {
                    answeringNavigator.goTo(ExercisesScreen(ExercisesScreen.Intent.SelectExercises))
                }

                is CreateWorkoutPlanUiEvent.OnAddSet -> {
                    val currentExercisePlan = workoutPlanSessionManager.currentPlan.value
                        ?.exercisePlanSessions
                        ?.find { exercisePlanSession -> exercisePlanSession.exercisePlan.id == event.exercisePlanId }

                    val nextIndex = currentExercisePlan?.setPlans?.size ?: 0

                    workoutPlanSessionManager.addSetPlanToExercise(
                        exercisePlanId = event.exercisePlanId,
                        newSetPlan = SetPlan(
                            id = 0,
                            exercisePlanId = event.exercisePlanId,
                            index = PositiveInt(nextIndex),
                            weight = 0,
                            reps = 0,
                        ),
                    )
                }

                is CreateWorkoutPlanUiEvent.OnSetWeightChanged -> {
                    workoutPlanSessionManager.updateSetPlanValue(
                        setPlanId = event.setId,
                        weight = event.setWeight,
                        reps = null,
                    )
                }

                is CreateWorkoutPlanUiEvent.OnSetRepsChanged -> {
                    workoutPlanSessionManager.updateSetPlanValue(
                        setPlanId = event.setId,
                        weight = null,
                        reps = event.setReps,
                    )
                }

                is CreateWorkoutPlanUiEvent.OnSaveWorkout -> {
                    scope.launch {
                        // Ensure the most up-to-date session from the manager is used for saving
                        workoutPlanSessionManager.currentPlan.value?.let { currentSessionToSave ->
                            saveWorkoutUseCase(
                                params = currentSessionToSave,
                            )
                            navigator.pop()
                        }
                    }
                }
            }
        }

        return CreateWorkoutPlanUiState(
            workoutPlanSession = workoutPlanSession,
            eventSink = ::eventSink,
        )
    }

}
