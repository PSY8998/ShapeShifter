package app.shapeshifter.data.supabase.datasources

import app.shapeshifter.data.models.Exercise
import app.shapeshifter.data.models.Muscles
import app.shapeshifter.data.supabase.responses.ExerciseResponse
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import me.tatarka.inject.annotations.Inject

interface ExerciseDataSource {
    suspend fun exercises(): List<Exercise>
}

@Inject
class SupabaseExerciseDataSource(
    override val supabase: SupabaseClient,
) : SupabaseDataSource,
    ExerciseDataSource {

    override suspend fun exercises(): List<Exercise> {
        val exercisesResponse: List<ExerciseResponse> = supabase.from(EXERCISE_TABLE)
            .select()
            .decodeList<ExerciseResponse>()

        return exercisesResponse.map { response ->
            Exercise(
                id = response.id,
                name = response.name,
                primaryMuscle = Muscles.safeValueOf(response.primaryMuscle),
                secondaryMuscle = response.secondaryMuscles?.map { Muscles.safeValueOf(it) }
                    ?: emptyList(),
                imageUrl = response.imageUrl ?: "",
            )
        }
    }

    companion object {
        private const val EXERCISE_TABLE = "exercises"
    }
}
