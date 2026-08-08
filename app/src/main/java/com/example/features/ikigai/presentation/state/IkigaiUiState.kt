package com.example.features.ikigai.presentation.state

import com.example.features.ikigai.data.dto.IkigaiResponse

sealed interface IkigaiUiState {
    data object Loading : IkigaiUiState
    data class Success(val data: IkigaiResponse) : IkigaiUiState
    data class Error(val message: String) : IkigaiUiState
}
