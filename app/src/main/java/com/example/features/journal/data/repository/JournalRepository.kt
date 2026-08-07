package com.example.features.journal.data.repository

import com.example.core.network.SupabaseClient
import com.example.core.network.dto.JournalEntryInsert
import io.github.jan.supabase.postgrest.postgrest

interface JournalRepository {
    suspend fun insertJournalEntry(entry: JournalEntryInsert): Result<Unit>
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
}
