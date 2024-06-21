package app.shapeshifter.feature.workout.ui.createworkoutplan

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import app.shapeshifter.feature.workout.domain.AddExerciseLogUseCase
import app.shapeshifter.feature.workout.domain.CreateWorkoutUseCase
import app.shapeshifter.feature.workout.domain.FetchExercisesUseCase
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuitx.effects.LaunchedImpressionEffect
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
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
) : Presenter<CreateWorkoutPlanUiState> {

    @Composable
    override fun present(): CreateWorkoutPlanUiState {

        val workoutPlan = remember {
            mutableStateOf(
                WorkoutPlan(
                    id = 0,
                    routineId = screen.routineId,
                    name = screen.planName,
                ),
            )
        }
        val exercisePlans = remember { mutableStateOf<List<ExercisePlan>>(emptyList()) }
        val exercises = remember { mutableStateOf<List<Exercise>>(emptyList()) }
        val setPlans = remember { mutableStateOf<List<SetPlan>>(emptyList()) }

        val scope = rememberCoroutineScope()

        val answeringNavigator =
            rememberAnsweringNavigator<ExercisesScreen.Result>(navigator) { result ->
                val selectedExerciseIds = result.exerciseIds
                exercises.value =
                    fetchExercisesUseCase(selectedExerciseIds).getOrNull() ?: emptyList()

                exercisePlans.value = selectedExerciseIds.map {
                    ExercisePlan(
                        id = 0,
                        workoutPlanId = 0,
                        exerciseId = it,
                        index = PositiveInt(0),
                    )
                }
            }

        fun eventSink(event: CreateWorkoutPlanUiEvent) {
            when (event) {
                is CreateWorkoutPlanUiEvent.OnAddExercise -> {
                    answeringNavigator.goTo(ExercisesScreen(true))
                }
            }
        }
        return CreateWorkoutPlanUiState(
            eventSink = ::eventSink,
            workoutPlanSession = WorkoutPlanSession(
                workoutPlan = workoutPlan.value,
                exercisePlanSessions = exercisePlans.value.map { plan ->
                    ExercisePlanSession(
                        exercisePlan = plan,
                        exercise = exercises.value.find { exercise -> exercise.id == plan.exerciseId }!!,
                        setPlans = emptyList(),
                    )
                },
            ),
        )
    }

}
