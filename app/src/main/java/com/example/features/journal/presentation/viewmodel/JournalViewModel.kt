package com.example.features.journal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.features.journal.presentation.state.JournalUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.features.journal.data.repository.JournalRepository
import com.example.features.journal.data.repository.JournalRepositoryImpl
import com.example.core.network.dto.JournalEntryInsert
import com.example.core.network.SupabaseClient
import io.github.jan.supabase.auth.auth

class JournalViewModel(
    private val repository: JournalRepository = JournalRepositoryImpl()
) : ViewModel() {
    private val _uiState = MutableStateFlow(JournalUiState())
    val uiState: StateFlow<JournalUiState> = _uiState.asStateFlow()

    fun onTextChanged(text: String) {
        _uiState.update { it.copy(journalText = text) }
    }

    fun onMessageShown() {
        _uiState.update { it.copy(errorMessage = null, isSuccess = false) }
    }

    fun onSaveEntryClicked() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, isSuccess = false) }
            
            val userId = SupabaseClient.client.auth.currentSessionOrNull()?.user?.id ?: ""
            if (userId.isEmpty()) {
                _uiState.update { it.copy(isSaving = false, errorMessage = "User not logged in") }
                return@launch
            }

            val entry = JournalEntryInsert(
                userId = userId,
                content = uiState.value.journalText
            )

            val result = repository.insertJournalEntry(entry)
            
            _uiState.update { 
                if (result.isSuccess) {
                    it.copy(
                        isSaving = false,
                        journalText = "",
                        errorMessage = null,
                        isSuccess = true
                    ) 
                } else {
                    it.copy(
                        isSaving = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Failed to save journal",
                        isSuccess = false
                    )
                }
            }
        }
    }
}
