package app.shapeshifter.feature.workout.ui.createworkoutplan

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.shapeshifter.common.ui.compose.screens.CreateWorkoutPlanScreen
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import app.shapeshifter.data.models.Exercise
import app.shapeshifter.data.models.PositiveInt
import app.shapeshifter.data.models.plans.ExercisePlan
import app.shapeshifter.data.models.plans.ExercisePlanSession
import app.shapeshifter.data.models.plans.SetPlan
import app.shapeshifter.data.models.plans.WorkoutPlan
import app.shapeshifter.data.models.plans.WorkoutPlanSession
import app.shapeshifter.feature.workout.domain.FetchExercisesUseCase
import app.shapeshifter.feature.workout.domain.FetchWorkoutPlanSessionsUseCase
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
import java.util.concurrent.atomic.AtomicInteger
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
    private val fetchExercisesUseCase: FetchExercisesUseCase,
    private val saveWorkoutUseCase: SaveWorkoutUseCase,
    private val selectWorkoutPlanUseCase: SelectWorkoutPlanUseCase,
) : Presenter<CreateWorkoutPlanUiState> {

    @Composable
    override fun present(): CreateWorkoutPlanUiState {

        var workoutPlanSession by rememberRetained { mutableStateOf<WorkoutPlanSession?>(null) }
        var workoutPlan by rememberRetained { mutableStateOf<WorkoutPlan?>(null) }
        var exercisePlans by rememberRetained { mutableStateOf<List<ExercisePlan>>(emptyList()) }
        var exercises by rememberRetained { mutableStateOf<List<Exercise>>(emptyList()) }
        var setPlans by rememberRetained { mutableStateOf<List<SetPlan>>(emptyList()) }

        val currentExercisePlanId = rememberSaveable { AtomicInteger(0) }

        val currentSetPlanId = rememberSaveable { AtomicInteger(0) }

        val scope = rememberCoroutineScope()

        val answeringNavigator =
            rememberAnsweringNavigator<ExercisesScreen.Result.SelectedExercises>(navigator) { result ->
                val selectedExerciseIds = result.exerciseIds
                exercises +=
                    fetchExercisesUseCase(selectedExerciseIds).getOrNull() ?: emptyList()

                exercisePlans += selectedExerciseIds.map {
                    ExercisePlan(
                        id = currentExercisePlanId.incrementAndGet().toLong(),
                        workoutPlanId = workoutPlan?.id ?: 0,
                        exerciseId = it,
                        index = PositiveInt(0),
                    )
                }
            }

        fun eventSink(event: CreateWorkoutPlanUiEvent) {
            when (event) {
                is CreateWorkoutPlanUiEvent.OnAddExercise -> {
                    answeringNavigator.goTo(ExercisesScreen(ExercisesScreen.Intent.SelectExercises))
                }

                is CreateWorkoutPlanUiEvent.OnAddSet -> {
                    setPlans += SetPlan(
                        id = currentSetPlanId.incrementAndGet().toLong(),
                        exercisePlanId = event.exercisePlanId,
                        index = PositiveInt(0),
                        weight = SetPlan.Undefined,
                        reps = SetPlan.Undefined,
                    )
                }

                is CreateWorkoutPlanUiEvent.OnSetWeightChanged -> {
                    setPlans = setPlans.map { plan ->
                        if (plan.id == event.setId) {
                            plan.copy(weight = event.setWeight)
                        } else
                            plan
                    }
                }

                is CreateWorkoutPlanUiEvent.OnSetRepsChanged -> {
                    setPlans = setPlans.map { plan ->
                        if (plan.id == event.setId) {
                            plan.copy(reps = event.setReps)
                        } else
                            plan
                    }
                }

                is CreateWorkoutPlanUiEvent.OnSaveWorkout -> {
                    scope.launch {
                        saveWorkoutUseCase(
                            params = event.workoutPlanSession,
                        )
                        navigator.pop()
                    }
                }
            }
        }

        LaunchedEffect(screen) {
            when (val intent = screen.intent) {
                is CreateWorkoutPlanScreen.Intent.NewWorkoutPlan -> {
                    workoutPlan = WorkoutPlan(
                        id = 0,
                        routineId = intent.routineId,
                        name = intent.planName,
                    )
                }

                is CreateWorkoutPlanScreen.Intent.EditWorkoutPlan -> {
                    val session = selectWorkoutPlanUseCase(
                        params = SelectWorkoutPlanUseCase.Params(intent.workoutPlanId),
                    ).getOrNull()
                    workoutPlan = session?.workoutPlan
                    exercisePlans = session?.exercisePlanSessions?.map { it.exercisePlan } ?: emptyList()
                    exercises = session?.exercisePlanSessions?.map { it.exercise } ?: emptyList()
                    setPlans = session?.exercisePlanSessions?.map { it.setPlans }?.flatten() ?: emptyList()
                }
            }
        }

        if (workoutPlan != null) {
            LaunchedEffect(workoutPlan, exercisePlans, setPlans, exercises) {
                workoutPlanSession = WorkoutPlanSession(
                    workoutPlan = workoutPlan!!,
                    exercisePlanSessions = exercisePlans.map { exercisePlan ->
                        ExercisePlanSession(
                            exercisePlan = exercisePlan,
                            exercise = exercises.find { it.id == exercisePlan.exerciseId }!!,
                            setPlans = setPlans.filter { it.exercisePlanId == exercisePlan.id },
                        )
                    },
                )
            }
        }


        return CreateWorkoutPlanUiState(
            workoutPlanSession = workoutPlanSession,
            eventSink = ::eventSink,
        )
    }

}
