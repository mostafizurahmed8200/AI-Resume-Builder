package com.ahmed.airesumebuilder.presentation.screens.resume

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmed.airesumebuilder.data.repository.ResumeRepository
import com.ahmed.airesumebuilder.domain.model.Resume
import com.ahmed.airesumebuilder.domain.usecase.AuthUseCases
import com.ahmed.airesumebuilder.domain.usecase.SaveResumeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ResumeUiState(
    val resume: Resume = Resume(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)


@HiltViewModel
class ResumeViewModel @Inject constructor(
    private val savedResumeUseCase: SaveResumeUseCase,
    private val resumeRepository: ResumeRepository,
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResumeUiState())
    val uiState = _uiState.asStateFlow()


    init {
        val userId = authUseCases.currentUser?.uid ?: ""
        _uiState.update { it.copy(resume = it.resume.copy(userId = userId)) }
    }

    fun loadResumes(resumeId: String) {
        viewModelScope.launch {
            resumeRepository.getResumeById(resumeId).filterNotNull().collect { resume ->
                _uiState.update { it.copy(resume = resume) }
            }
        }
    }





}