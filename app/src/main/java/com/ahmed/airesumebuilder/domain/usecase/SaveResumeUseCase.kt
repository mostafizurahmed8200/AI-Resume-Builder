package com.ahmed.airesumebuilder.domain.usecase

import com.ahmed.airesumebuilder.data.repository.ResumeRepository
import com.ahmed.airesumebuilder.domain.model.Resume
import com.ahmed.airesumebuilder.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveResumeUseCase @Inject constructor(
    private val resumeRepository: ResumeRepository
) {
    suspend operator fun invoke(resume: Resume, syncToCloud: Boolean = true): Resource<Unit> {

        if (resume.personalInfo.fullName.isBlank()) {
            return Resource.Error("Full name is required to save the resume.")
        }
        return resumeRepository.saveResume(resume, syncToCloud)

    }


}