package com.ahmed.airesumebuilder.presentation.screens.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmed.airesumebuilder.data.repository.ResumeRepository
import com.ahmed.airesumebuilder.domain.model.ATSAnalysisResult
import com.ahmed.airesumebuilder.domain.model.Resume
import com.ahmed.airesumebuilder.domain.usecase.GenerateResumeUseCase
import com.ahmed.airesumebuilder.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class AIUiState(
    val resume: Resume? = null,
    val isLoading: Boolean = false,
    val enhanceSummery: String? = null,
    val skillSuggestion: List<String> = emptyList(),
    val coverLetter: String? = null,
    val atsResult: ATSAnalysisResult? = null,
    val improvement: List<String> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class AIViewModel @Inject constructor(
    private val generateResumeUseCase: GenerateResumeUseCase,
    private val resumeRepository: ResumeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AIUiState())
    val uiState = _uiState.asStateFlow()

    fun loadResume(resumeId: String) {
        viewModelScope.launch {
            resumeRepository.getResumeById(resumeId).filterNotNull().collect { resume ->
                _uiState.update {
                    it.copy(resume = resume)
                }
            }
        }
    }

    //Enhance Summery
    fun enhanceSummery() {
        val resume = _uiState.value.resume ?: return
        val jobTitle = resume.experiences.firstOrNull()?.jobTitle ?: "Professional"

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = generateResumeUseCase.enhanceSummery(
                resume.personalInfo.summary, jobTitle
            )) {
                is Resource.Success -> _uiState.update {
                    it.copy(isLoading = false, enhanceSummery = result.data)
                }

                is Resource.Error -> _uiState.update {
                    it.copy(
                        isLoading = false, errorMessage = result.message
                    )
                }

                is Resource.Loading -> Unit

            }


        }


    }


    // SKill
    fun getSkillSuggestions() {
        val resume = _uiState.value.resume ?: return
        val jobTitle = resume.experiences.firstOrNull()?.jobTitle ?: "Professional"

        val currentSkills = resume.skills.map { it.name }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = generateResumeUseCase.getSkillSuggestion(jobTitle, currentSkills)) {
                is Resource.Success -> _uiState.update {
                    it.copy(isLoading = false, skillSuggestion = result.data ?: emptyList())
                }

                is Resource.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }

                is Resource.Loading -> Unit
            }
        }

    }


    //Cover Later
    fun generateCoverLater(companyName: String, jobDescription: String) {
        val resume = _uiState.value.resume ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result =
                generateResumeUseCase.generateCoverLater(resume, companyName, jobDescription)) {
                is Resource.Success -> _uiState.update {
                    it.copy(isLoading = false, coverLetter = result.data)
                }

                is Resource.Error -> _uiState.update {
                    it.copy(
                        isLoading = false, errorMessage = result.message
                    )
                }

                is Resource.Loading -> Unit

            }
        }
    }


    //Analyze ATS

    fun analyzeATS(jobDescription: String) {
        val resume = _uiState.value.resume ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = generateResumeUseCase.analyzeATS(resume, jobDescription)) {
                is Resource.Success -> _uiState.update {
                    it.copy(
                        isLoading = false, atsResult = result.data
                    )
                }

                is Resource.Error -> _uiState.update {
                    it.copy(
                        isLoading = false, errorMessage = result.message
                    )
                }

                is Resource.Loading -> Unit

            }

        }
    }


    // Improvement
    fun getImprovement() {
        val resume = _uiState.value.resume ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = generateResumeUseCase.suggestImprovements(resume)) {
                is Resource.Success -> _uiState.update {
                    it.copy(
                        isLoading = false, improvement = result.data ?: emptyList()
                    )
                }

                is Resource.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }

                is Resource.Loading -> Unit

            }

        }
    }

    fun clearError() = _uiState.update { it.copy(errorMessage = null) }

}