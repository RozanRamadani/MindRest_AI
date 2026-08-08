package com.example.features.mood.data.repository

import com.example.core.network.SupabaseClient
import com.example.core.network.dto.MoodLog
import com.example.core.network.dto.MoodLogInsert
import io.github.jan.supabase.postgrest.postgrest

interface MoodRepository {
    suspend fun insertMoodLog(log: MoodLogInsert): Result<Unit>
    suspend fun getRecentMoodLogs(): Result<List<MoodLog>>
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

    override suspend fun getRecentMoodLogs(): Result<List<MoodLog>> {
        return try {
            val response = SupabaseClient.client.postgrest["mood_logs"]
                .select {
                    order("created_at", io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                }.decodeList<MoodLog>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
