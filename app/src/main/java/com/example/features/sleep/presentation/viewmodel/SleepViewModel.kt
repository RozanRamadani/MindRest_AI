package com.example.features.sleep.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.features.sleep.presentation.state.SleepQuality
import com.example.features.sleep.presentation.state.SleepUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import java.util.Calendar
import java.util.TimeZone
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.core.network.SupabaseClient
import com.example.core.network.dto.SleepLogInsert
import com.example.features.sleep.data.repository.SleepRepository
import com.example.features.sleep.data.repository.SleepRepositoryImpl
import io.github.jan.supabase.auth.auth

class SleepViewModel(
    private val repository: SleepRepository = SleepRepositoryImpl()
) : ViewModel() {
    private val _uiState = MutableStateFlow(SleepUiState())
    val uiState: StateFlow<SleepUiState> = _uiState.asStateFlow()

    fun onBedTimeChanged(time: String) {
        _uiState.update { 
            it.copy(
                bedTime = time,
                totalSleepDuration = calculateSleepDuration(time, it.wakeUpTime)
            ) 
        }
    }

    fun onWakeUpTimeChanged(time: String) {
        _uiState.update { 
            it.copy(
                wakeUpTime = time,
                totalSleepDuration = calculateSleepDuration(it.bedTime, time)
            ) 
        }
    }

    fun onSleepQualityChanged(quality: SleepQuality) {
        _uiState.update { it.copy(sleepQuality = quality) }
    }

    fun onMessageShown() {
        _uiState.update { it.copy(errorMessage = null, isSuccess = false) }
    }

    fun onSaveClicked() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, isSuccess = false) }
            
            val userId = SupabaseClient.client.auth.currentSessionOrNull()?.user?.id ?: ""
            if (userId.isEmpty()) {
                _uiState.update { it.copy(isSaving = false, errorMessage = "User not logged in") }
                return@launch
            }

            val current = uiState.value
            val logInsert = SleepLogInsert(
                userId = userId,
                bedTime = formatTimeToIsoString(current.bedTime),
                wakeUpTime = formatTimeToIsoString(current.wakeUpTime),
                sleepQuality = current.sleepQuality.name
            )

            val result = repository.insertSleepLog(logInsert)
            
            _uiState.update {
                if (result.isSuccess) {
                    it.copy(isSaving = false, errorMessage = null, isSuccess = true)
                } else {
                    it.copy(isSaving = false, errorMessage = result.exceptionOrNull()?.message ?: "Failed to save sleep log", isSuccess = false)
                }
            }
        }
    }

    private fun calculateSleepDuration(bedTime: String, wakeUpTime: String): String {
        try {
            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            val bedDate: Date = format.parse(bedTime) ?: return "0 hours 0 minutes"
            var wakeDate: Date = format.parse(wakeUpTime) ?: return "0 hours 0 minutes"

            if (wakeDate.before(bedDate)) {
                // Assuming wake up time is on the next day if it's before bed time
                wakeDate = Date(wakeDate.time + (1000 * 60 * 60 * 24))
            }

            val diffMs = wakeDate.time - bedDate.time
            val diffHours = diffMs / (1000 * 60 * 60)
            val diffMinutes = (diffMs / (1000 * 60)) % 60

            return "$diffHours hours $diffMinutes minutes"
        } catch (e: Exception) {
            return "Invalid time format"
        }
    }

    private fun formatTimeToIsoString(time: String): String {
        try {
            val parts = time.split(":")
            val hour = parts[0].toInt()
            val minute = parts[1].toInt()
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            isoFormat.timeZone = TimeZone.getTimeZone("UTC")
            return isoFormat.format(calendar.time)
        } catch (e: Exception) {
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            isoFormat.timeZone = TimeZone.getTimeZone("UTC")
            return isoFormat.format(Date())
        }
    }
}
