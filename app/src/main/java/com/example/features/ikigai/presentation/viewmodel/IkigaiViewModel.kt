package com.example.features.ikigai.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.features.ikigai.data.repository.AiInsightRepositoryImpl
import com.example.features.ikigai.domain.repository.AiInsightRepository
import com.example.features.ikigai.presentation.state.IkigaiUiState
import com.example.features.journal.data.repository.JournalRepository
import com.example.features.journal.data.repository.JournalRepositoryImpl
import com.example.features.mood.data.repository.MoodRepository
import com.example.features.mood.data.repository.MoodRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IkigaiViewModel(
    private val aiInsightRepository: AiInsightRepository = AiInsightRepositoryImpl(),
    private val journalRepository: JournalRepository = JournalRepositoryImpl(),
    private val moodRepository: MoodRepository = MoodRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<IkigaiUiState>(IkigaiUiState.Loading)
    val uiState: StateFlow<IkigaiUiState> = _uiState.asStateFlow()

    init {
        fetchIkigaiInsight()
    }

    fun fetchIkigaiInsight() {
        viewModelScope.launch {
            _uiState.update { IkigaiUiState.Loading }

            val journalsResult = journalRepository.getRecentJournals()
            val moodLogsResult = moodRepository.getRecentMoodLogs()

            val journals = journalsResult.getOrNull() ?: emptyList()
            val moodLogs = moodLogsResult.getOrNull() ?: emptyList()

            if (journals.isEmpty() && moodLogs.isEmpty()) {
                _uiState.update { IkigaiUiState.Error("Tidak ada data jurnal atau mood yang cukup untuk dianalisis.") }
                return@launch
            }

            val insightResult = aiInsightRepository.generateIkigaiDashboard(journals, moodLogs)
            
            insightResult.onSuccess { data ->
                _uiState.update { IkigaiUiState.Success(data) }
            }.onFailure { error ->
                _uiState.update { IkigaiUiState.Error(error.message ?: "Terjadi kesalahan saat menghubungi Gemini AI.") }
            }
        }
    }
}
