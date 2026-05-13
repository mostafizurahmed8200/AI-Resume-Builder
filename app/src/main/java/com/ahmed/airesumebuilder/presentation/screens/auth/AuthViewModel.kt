package com.ahmed.airesumebuilder.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmed.airesumebuilder.domain.usecase.AuthUseCases
import com.ahmed.airesumebuilder.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val isRegistrationSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()


    val isLoggedIn: Boolean = authUseCases.isLoggedIn

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            when (val result = authUseCases.login(email, password)) {
                is Resource.Success -> _uiState.value = AuthUiState(isLoginSuccess = true)
                is Resource.Error -> _uiState.value = AuthUiState(errorMessage = result.message)
                is Resource.Loading -> Unit
            }


        }
    }

    fun register(email: String, password: String, displayName: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            when (val result = authUseCases.register(email, password, displayName)) {
                is Resource.Success -> {
                    authUseCases.logout() // Force logout after registration
                    _uiState.value = AuthUiState(isRegistrationSuccess = true)
                }
                is Resource.Error -> _uiState.value = AuthUiState(errorMessage = result.message)
                is Resource.Loading -> Unit

            }


        }


    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            when (val result = authUseCases.resetPassword(email)) {
                is Resource.Success -> _uiState.value = AuthUiState(isLoginSuccess = true) // Reusing success for simplicity or add isResetSuccess
                is Resource.Error -> _uiState.value = AuthUiState(errorMessage = result.message)
                is Resource.Loading -> Unit
            }
        }
    }

    fun logOut() = viewModelScope.launch {
        authUseCases.logout()
    }

    fun clearState() {
        _uiState.value = AuthUiState()
    }
}