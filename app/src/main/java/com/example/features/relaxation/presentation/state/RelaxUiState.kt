package com.example.features.relaxation.presentation.state

import kotlinx.serialization.Serializable

data class RelaxUiState(
    val mediaItems: List<RelaxMediaItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@Serializable
data class RelaxMediaItem(
    val id: String,
    val title: String,
    val category: RelaxCategory,
    val thumbnailUrl: String = "" // Placeholder for thumbnail URL
)

@Serializable
enum class RelaxCategory(val displayName: String) {
    VIDEO("Video Relaksasi"),
    MUSIC("Musik Fokus"),
    MEDITATION("Meditasi")
}
