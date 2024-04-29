package app.shapeshifter.data.supabase

import app.shapeshifter.core.base.inject.ApplicationScope
import app.shapeshifter.data.supabase.datasources.ExerciseDataSource
import app.shapeshifter.data.supabase.datasources.SupabaseExerciseDataSource
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.logging.LogLevel
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import me.tatarka.inject.annotations.Provides
import kotlinx.serialization.json.Json

interface SupabaseComponent {

    @ApplicationScope
    @Provides
    fun provideSupabaseClient(): SupabaseClient =
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_KEY,
        ) {
            install(Postgrest)
            defaultSerializer = KotlinXSerializer(
                Json {
                    ignoreUnknownKeys = true
                },
            )
            defaultLogLevel = LogLevel.DEBUG
        }


    @ApplicationScope
    @Provides
    fun provideExerciseDataSource(
        dataSource: SupabaseExerciseDataSource,
    ): ExerciseDataSource = dataSource
}
