package com.example.features.ikigai.domain.repository

import com.example.core.network.dto.JournalEntry
import com.example.core.network.dto.MoodLog
import com.example.features.ikigai.data.dto.IkigaiResponse

interface AiInsightRepository {
    suspend fun generateIkigaiDashboard(
        journals: List<JournalEntry>,
        moodLogs: List<MoodLog>
    ): Result<IkigaiResponse>
}
