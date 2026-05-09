package com.ahmed.airesumebuilder.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Resume
    (
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val personalInfo: PersonalInfo = PersonalInfo(),
    val education: List<Education> = emptyList(),
    val experiences: List<Experience> = emptyList(),
    val skills: List<Skill> = emptyList(),
    val projects: List<Project> = emptyList(),

    val templateId: String = "classic",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
) : Parcelable
