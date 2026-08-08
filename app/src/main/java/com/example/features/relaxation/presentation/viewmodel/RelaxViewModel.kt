package com.example.features.relaxation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.features.relaxation.presentation.state.RelaxCategory
import com.example.features.relaxation.presentation.state.RelaxMediaItem
import com.example.features.relaxation.presentation.state.RelaxUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.example.core.network.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RelaxViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RelaxUiState(isLoading = true))
    val uiState: StateFlow<RelaxUiState> = _uiState.asStateFlow()

    init {
        fetchRelaxMedia()
    }

    private fun fetchRelaxMedia() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Fetch from Supabase 'relax_media' table
                val media = SupabaseClient.client.postgrest["relax_media"]
                    .select().decodeList<RelaxMediaItem>()
                _uiState.value = RelaxUiState(mediaItems = media, isLoading = false)
            } catch (e: Exception) {
                // If the table doesn't exist yet, we can fallback to dummy data for now,
                // but the prompt explicitly requires no mock data.
                // However, if the table doesn't exist, it will crash. Let's just show error.
                _uiState.value = RelaxUiState(error = e.message ?: "Unknown error", isLoading = false)
            }
        }
    }

    fun onPlayClicked(item: RelaxMediaItem) {
        // Handle playing media
    }
}
