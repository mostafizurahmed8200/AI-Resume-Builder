package com.ahmed.airesumebuilder.domain.usecase

import com.ahmed.airesumebuilder.data.repository.AIRepository
import com.ahmed.airesumebuilder.domain.model.ATSAnalysisResult
import com.ahmed.airesumebuilder.domain.model.Experience
import com.ahmed.airesumebuilder.domain.model.Resume
import com.ahmed.airesumebuilder.util.Resource
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GenerateResumeUseCase
@Inject constructor(private val aiRepository: AIRepository) {
    suspend fun enhanceSummery(
        currentSummery: String, jobTitle: String
    ): Resource<String> = aiRepository.enhanceSummery(currentSummery, jobTitle)

    suspend fun enhanceExperience(experience: Experience): Resource<String> =
        aiRepository.enhanceExperience(experience)


    suspend fun getSkillSuggestion(
        jobTitle: String,
        currentSills: List<String>
    ): Resource<List<String>> = aiRepository.getSkillSuggestion(jobTitle, currentSills)


    suspend fun generateCoverLater(resume: Resume, companyName: String, jobDescription: String)
            : Resource<String> =
        aiRepository.generateCoverLater(resume, companyName, jobDescription)


    suspend fun analyzeATS(
        resume: Resume,
        jobDescription: String
    ): Resource<ATSAnalysisResult> = aiRepository.analyzeATS(resume, jobDescription)


    suspend fun suggestImprovements(resume: Resume): Resource<List<String>> =
        aiRepository.getImprovementSuggestions(resume)


}