package com.example.features.sleep.presentation.state

data class SleepUiState(
    val bedTime: String = "22:00",
    val wakeUpTime: String = "06:00",
    val sleepQuality: SleepQuality = SleepQuality.GOOD,
    val totalSleepDuration: String = "8 hours 0 minutes",
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

enum class SleepQuality {
    POOR, FAIR, GOOD, EXCELLENT
}
