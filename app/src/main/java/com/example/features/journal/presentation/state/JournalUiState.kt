package com.example.features.journal.presentation.state

data class JournalUiState(
    val journalText: String = "",
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)
