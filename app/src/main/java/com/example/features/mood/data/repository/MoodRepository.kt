package com.example.features.mood.data.repository

import com.example.core.network.SupabaseClient
import com.example.core.network.dto.MoodLogInsert
import io.github.jan.supabase.postgrest.postgrest

interface MoodRepository {
    suspend fun insertMoodLog(log: MoodLogInsert): Result<Unit>
}

class MoodRepositoryImpl : MoodRepository {
    override suspend fun insertMoodLog(log: MoodLogInsert): Result<Unit> {
        return try {
            SupabaseClient.client.postgrest["mood_logs"].insert(log)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
