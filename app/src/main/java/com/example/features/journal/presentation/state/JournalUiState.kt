package com.example.features.journal.presentation.state

import com.example.core.network.dto.JournalEntry

data class JournalUiState(
    val journalText: String = "",
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val journalHistory: List<JournalEntry> = emptyList(),
    val isLoadingHistory: Boolean = false
)
