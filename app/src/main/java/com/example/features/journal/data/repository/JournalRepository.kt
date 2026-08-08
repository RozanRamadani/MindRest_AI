package com.example.features.journal.data.repository

import com.example.core.network.SupabaseClient
import com.example.core.network.dto.JournalEntry
import com.example.core.network.dto.JournalEntryInsert
import io.github.jan.supabase.postgrest.postgrest

interface JournalRepository {
    suspend fun insertJournalEntry(entry: JournalEntryInsert): Result<Unit>
    suspend fun getRecentJournals(): Result<List<JournalEntry>>
}

class JournalRepositoryImpl : JournalRepository {
    override suspend fun insertJournalEntry(entry: JournalEntryInsert): Result<Unit> {
        return try {
            SupabaseClient.client.postgrest["journal_entries"].insert(entry)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecentJournals(): Result<List<JournalEntry>> {
        return try {
            val response = SupabaseClient.client.postgrest["journal_entries"]
                .select {
                    order("created_at", io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                }.decodeList<JournalEntry>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
