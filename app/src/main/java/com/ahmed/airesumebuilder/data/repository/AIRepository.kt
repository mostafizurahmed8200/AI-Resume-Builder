package com.ahmed.airesumebuilder.data.repository

import com.ahmed.airesumebuilder.data.remote.GeminiService
import com.ahmed.airesumebuilder.domain.model.ATSAnalysisResult
import com.ahmed.airesumebuilder.domain.model.Experience
import com.ahmed.airesumebuilder.domain.model.Resume
import com.ahmed.airesumebuilder.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIRepository @Inject
constructor(
    private val geminiService: GeminiService
) {
    suspend fun enhanceSummery(
        currentSummery: String,
        jobTitle: String
    ): Resource<String> {
        return try {
            val result = geminiService.enhanceSummery(currentSummery, jobTitle)
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to enhance summery")
        }
    }

    suspend fun enhanceExperience(experience: Experience): Resource<String> {
        return try {
            val result = geminiService.enhanceExperienceDescription(experience)
            Resource.Success(result)

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to enhance experience")
        }

    }

    suspend fun getSkillSuggestion(
        jobTitle: String,
        currentSkills: List<String>
    ): Resource<List<String>> {
        return try {

            val result = geminiService.generateSkillSuggestion(jobTitle, currentSkills)
            Resource.Success(result)

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to get skill suggestions")
        }

    }


    suspend fun generateCoverLater(
        resume: Resume,
        companyName: String,
        jobDescription: String,
    ): Resource<String> {

        return try {
            val result = geminiService.generateCoverLater(resume, companyName, jobDescription)
            Resource.Success(result)

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to generate cover letter")
        }


    }


    suspend fun analyzeATS(
        resume: Resume,
        jobDescription: String
    ): Resource<ATSAnalysisResult> {
        return try {
            val result = geminiService.analysisResumeForATS(resume, jobDescription)
            Resource.Success(result)

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to analyze resume")
        }

    }


    suspend fun getImprovementSuggestions(resume: Resume): Resource<List<String>> {
        return try {
            val result = geminiService.suggestImprovements(resume)
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to get suggestions")
        }

    }


}


