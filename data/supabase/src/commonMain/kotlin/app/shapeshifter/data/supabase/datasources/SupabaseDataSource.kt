package app.shapeshifter.data.supabase.datasources

import io.github.jan.supabase.SupabaseClient

interface SupabaseDataSource {
    val supabase: SupabaseClient
}
