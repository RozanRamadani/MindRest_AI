package com.example.features.sleep.data.repository

import com.example.core.network.SupabaseClient
import com.example.core.network.dto.SleepLog
import com.example.core.network.dto.SleepLogInsert
import io.github.jan.supabase.postgrest.postgrest

interface SleepRepository {
    suspend fun insertSleepLog(log: SleepLogInsert): Result<Unit>
    suspend fun getRecentSleepLogs(): Result<List<SleepLog>>
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

    override suspend fun getRecentSleepLogs(): Result<List<SleepLog>> {
        return try {
            val response = SupabaseClient.client.postgrest["sleep_logs"]
                .select {
                    order("created_at", io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                }.decodeList<SleepLog>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
