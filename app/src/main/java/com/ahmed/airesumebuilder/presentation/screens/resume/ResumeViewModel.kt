package com.ahmed.airesumebuilder.presentation.screens.resume

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmed.airesumebuilder.data.repository.ResumeRepository
import com.ahmed.airesumebuilder.domain.model.Education
import com.ahmed.airesumebuilder.domain.model.Experience
import com.ahmed.airesumebuilder.domain.model.PersonalInfo
import com.ahmed.airesumebuilder.domain.model.Project
import com.ahmed.airesumebuilder.domain.model.Resume
import com.ahmed.airesumebuilder.domain.model.Skill
import com.ahmed.airesumebuilder.domain.usecase.AuthUseCases
import com.ahmed.airesumebuilder.domain.usecase.SaveResumeUseCase
import com.ahmed.airesumebuilder.util.Resource
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

    fun updatePersonalInfo(personalInfo: PersonalInfo) {
        _uiState.update {
            it.copy(resume = it.resume.copy(personalInfo = personalInfo))
        }

    }

    //Education--
    fun addEducation(education: Education) {
        _uiState.update { it.copy(resume = it.resume.copy(education = it.resume.education + education)) }
    }

    fun updateEducation(education: Education) {
        _uiState.update {
            it.copy(
                resume = it.resume.copy(
                    education = it.resume.education.map { e -> if (e.id == education.id) education else e })
            )
        }
    }

    fun removeEducation(educationId: String) {
        _uiState.update {
            it.copy(
                resume = it.resume.copy(
                    education = it.resume.education.filter { e -> e.id != educationId })
            )
        }
    }

    //Experience
    fun addExperience(experience: Experience) {
        _uiState.update {
            it.copy(
                resume = it.resume.copy(
                    experiences = it.resume.experiences + experience
                )
            )
        }
    }

    fun updateExperience(experience: Experience) {
        _uiState.update {
            it.copy(
                resume = it.resume.copy(
                    experiences = it.resume.experiences.map { e ->
                        if (e.id == experience.id) experience else e
                    })
            )
        }
    }

    fun removeExperience(experienceId: String) {
        _uiState.update {
            it.copy(
                resume = it.resume.copy(
                    experiences = it.resume.experiences.filter { e ->
                        e.id != experienceId
                    })
            )
        }
    }

    // Skills

    fun addSkills(skill: Skill) {
        _uiState.update { it.copy(resume = it.resume.copy(skills = it.resume.skills + skill)) }
    }

    fun removeSkills(skillId: String) {
        _uiState.update {
            it.copy(
                resume = it.resume.copy(
                    skills = it.resume.skills.filter { e ->
                        e.id != skillId
                    })
            )
        }

    }

    //Project

    fun addProject(project: Project) {
        _uiState.update {
            it.copy(resume = it.resume.copy(projects = it.resume.projects + project))
        }
    }

    fun removeProject(projectId: String) {
        _uiState.update {
            it.copy(
                resume = it.resume.copy(
                    projects = it.resume.projects.filter
                    { e ->
                        e.id != projectId
                    })
            )
        }
    }

    // Template----
    fun selectTemplate(templateId: String) {
        _uiState.update { it.copy(resume = it.resume.copy(templateId = templateId)) }
    }

    fun saveResume(syncToCloud: Boolean = false) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            when (val result = savedResumeUseCase(_uiState.value.resume, syncToCloud)) {
                is Resource.Success -> _uiState.update {
                    it.copy(isLoading = false, isSaved = true)
                }

                is Resource.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }

                is Resource.Loading -> Unit

            }
        }
    }


    fun clearError() = _uiState.update { it.copy(errorMessage = null, isSaved = false) }

}

