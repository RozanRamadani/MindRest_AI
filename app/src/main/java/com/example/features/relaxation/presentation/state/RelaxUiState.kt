package com.example.features.relaxation.presentation.state

data class RelaxUiState(
    val mediaItems: List<RelaxMediaItem> = emptyList()
)

data class RelaxMediaItem(
    val id: String,
    val title: String,
    val category: RelaxCategory,
    val thumbnailUrl: String = "" // Placeholder for thumbnail URL
)

enum class RelaxCategory(val displayName: String) {
    VIDEO("Video Relaksasi"),
    MUSIC("Musik Fokus"),
    MEDITATION("Meditasi")
}
