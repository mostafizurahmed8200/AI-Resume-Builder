package com.ahmed.airesumebuilder.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmed.airesumebuilder.domain.model.User
import com.ahmed.airesumebuilder.domain.usecase.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isLoggedOut: Boolean = false

)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUser()
    }

    private fun loadUser() {
        val uid = authUseCases.currentUser?.uid ?: return
        viewModelScope.launch {
            authUseCases.getLocalUser(uid)
                .filterNotNull()
                .collect { user ->
                    _uiState.update { it.copy(user = user) }
                }
        }

    }

    fun logOut() {
        viewModelScope.launch {
            authUseCases.logout()
            _uiState.update { it.copy(isLoading = true) }
        }
    }


}