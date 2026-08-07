package com.example.features.sleep.data.repository

import com.example.core.network.SupabaseClient
import com.example.core.network.dto.SleepLogInsert
import io.github.jan.supabase.postgrest.postgrest

interface SleepRepository {
    suspend fun insertSleepLog(log: SleepLogInsert): Result<Unit>
}

class SleepRepositoryImpl : SleepRepository {
    override suspend fun insertSleepLog(log: SleepLogInsert): Result<Unit> {
        return try {
            SupabaseClient.client.postgrest["sleep_logs"].insert(log)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
