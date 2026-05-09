package com.ahmed.airesumebuilder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ahmed.airesumebuilder.data.local.Converters
import com.ahmed.airesumebuilder.domain.model.Education
import com.ahmed.airesumebuilder.domain.model.Experience
import com.ahmed.airesumebuilder.domain.model.PersonalInfo
import com.ahmed.airesumebuilder.domain.model.Project
import com.ahmed.airesumebuilder.domain.model.Resume
import com.ahmed.airesumebuilder.domain.model.Skill

@Entity(tableName = "resumes")
@TypeConverters(Converters::class)
data class ResumeEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val personalInfo: PersonalInfo,
    val education: List<Education>,
    val experiences: List<Experience>,
    val skills: List<Skill>,
    val projects: List<Project>,
    val templateId: String,
    val createdAt: Long,
    val updatedAt: Long,
    val isSynced: Boolean
) {
    fun toDomain(): Resume = Resume(
        id = id,
        userId = userId,
        personalInfo = personalInfo,
        education = education,
        experiences = experiences,
        skills = skills,
        projects = projects,
        templateId = templateId,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isSynced = isSynced
    )

    companion object {
        fun fromDomain(resume: Resume): ResumeEntity = ResumeEntity(
            id = resume.id,
            userId = resume.userId,
            personalInfo = resume.personalInfo,
            education = resume.education,
            experiences = resume.experiences,
            skills = resume.skills,
            projects = resume.projects,
            templateId = resume.templateId,
            createdAt = resume.createdAt,
            updatedAt = resume.updatedAt,
            isSynced = resume.isSynced
        )


    }


}