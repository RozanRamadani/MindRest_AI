package com.example.features.authentication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.features.authentication.data.repository.AuthRepository
import com.example.features.authentication.data.repository.AuthRepositoryImpl
import com.example.features.authentication.presentation.state.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onMessageShown() {
        _uiState.update { it.copy(errorMessage = null, isSuccess = false) }
    }

    fun signIn(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email and password cannot be empty") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }
            val result = repository.signIn(email, pass)
            _uiState.update {
                if (result.isSuccess) {
                    it.copy(isLoading = false, isSuccess = true)
                } else {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Failed to sign in"
                    )
                }
            }
        }
    }

    fun signUp(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email and password cannot be empty") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }
            val result = repository.signUp(email, pass)
            _uiState.update {
                if (result.isSuccess) {
                    it.copy(isLoading = false, isSuccess = true)
                } else {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Failed to sign up"
                    )
                }
            }
        }
    }
}
