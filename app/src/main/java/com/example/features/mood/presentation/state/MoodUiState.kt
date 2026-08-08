package com.example.features.mood.presentation.state

import com.example.core.network.dto.MoodLog

data class MoodUiState(
    val selectedMood: Int? = null, // 1 to 5 scale (1: Terrible, 5: Awesome)
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val moodHistory: List<MoodLog> = emptyList(),
    val isLoadingHistory: Boolean = false
)
