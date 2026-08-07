package com.example.features.relaxation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.features.relaxation.presentation.state.RelaxCategory
import com.example.features.relaxation.presentation.state.RelaxMediaItem
import com.example.features.relaxation.presentation.state.RelaxUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RelaxViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        RelaxUiState(
            mediaItems = getDummyMediaItems()
        )
    )
    val uiState: StateFlow<RelaxUiState> = _uiState.asStateFlow()

    fun onPlayClicked(item: RelaxMediaItem) {
        // Handle playing media
    }

    private fun getDummyMediaItems(): List<RelaxMediaItem> {
        return listOf(
            RelaxMediaItem("1", "Hutan Hujan Tropis", RelaxCategory.VIDEO),
            RelaxMediaItem("2", "Suara Ombak", RelaxCategory.VIDEO),
            RelaxMediaItem("3", "Binaural Beats Focus", RelaxCategory.MUSIC),
            RelaxMediaItem("4", "Lo-Fi Study", RelaxCategory.MUSIC),
            RelaxMediaItem("5", "Meditasi Pagi", RelaxCategory.MEDITATION),
            RelaxMediaItem("6", "Meditasi Tidur Lelap", RelaxCategory.MEDITATION)
        )
    }
}
