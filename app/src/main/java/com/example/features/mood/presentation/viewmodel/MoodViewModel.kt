package com.example.features.mood.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.features.mood.presentation.state.MoodUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.core.network.SupabaseClient
import com.example.core.network.dto.MoodLogInsert
import com.example.features.mood.data.repository.MoodRepository
import com.example.features.mood.data.repository.MoodRepositoryImpl
import io.github.jan.supabase.auth.auth

class MoodViewModel(
    private val repository: MoodRepository = MoodRepositoryImpl()
) : ViewModel() {
    private val _uiState = MutableStateFlow(MoodUiState())
    val uiState: StateFlow<MoodUiState> = _uiState.asStateFlow()

    fun onMoodSelected(mood: Int) {
        _uiState.update { it.copy(selectedMood = mood) }
    }

    fun onMessageShown() {
        _uiState.update { it.copy(errorMessage = null, isSuccess = false) }
    }

    fun onSaveMoodClicked() {
        viewModelScope.launch {
            val mood = uiState.value.selectedMood ?: return@launch
            
            _uiState.update { it.copy(isSaving = true, errorMessage = null, isSuccess = false) }
            
            val userId = SupabaseClient.client.auth.currentSessionOrNull()?.user?.id ?: ""
            if (userId.isEmpty()) {
                _uiState.update { it.copy(isSaving = false, errorMessage = "User not logged in") }
                return@launch
            }

            val logInsert = MoodLogInsert(
                userId = userId,
                moodScore = mood
            )

            val result = repository.insertMoodLog(logInsert)
            
            _uiState.update {
                if (result.isSuccess) {
                    it.copy(isSaving = false, errorMessage = null, selectedMood = null, isSuccess = true)
                } else {
                    it.copy(isSaving = false, errorMessage = result.exceptionOrNull()?.message ?: "Failed to save mood", isSuccess = false)
                }
            }
        }
    }
}
