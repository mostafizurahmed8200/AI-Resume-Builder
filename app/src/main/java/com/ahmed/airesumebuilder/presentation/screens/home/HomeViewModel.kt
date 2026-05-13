package com.ahmed.airesumebuilder.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmed.airesumebuilder.data.repository.ResumeRepository
import com.ahmed.airesumebuilder.domain.model.Resume
import com.ahmed.airesumebuilder.domain.usecase.AuthUseCases
import com.ahmed.airesumebuilder.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class HomeUiState(
    val resumes: List<Resume> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val resumeRepository: ResumeRepository,
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val currentUser get() = authUseCases.currentUser

    init {
        loadResumes()
    }

    private fun loadResumes() {
        val userId = authUseCases.currentUser?.uid ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            resumeRepository.getLocalResumes(userId)
                .catch { e ->
                    // If something breaks, stop loading and show the error
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message
                        )
                    }
                }.collect { resumes ->
                    // When new data arrives, put it in the state
                    _uiState.update {
                        it.copy(
                            resumes = resumes, isLoading = false
                        )
                    }
                }

        }

    }


    //Delete Resumes

    fun deleteResume(resumeId: String) {
        viewModelScope.launch {
            when (val result = resumeRepository.deleteResume(resumeId)) {
                is Resource.Error -> _uiState.update {
                    it.copy(
                        errorMessage = result.message
                    )
                }

                else -> Unit

            }


        }
    }


    //Sync Resumes
    fun syncResumes() {
        val userId = authUseCases.currentUser?.uid ?: return
        viewModelScope.launch { resumeRepository.syncResumes(userId) }

    }

    //Clear Error

    fun clearError() = _uiState.update { it.copy(errorMessage = null) }


}