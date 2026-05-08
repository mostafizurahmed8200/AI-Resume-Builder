package com.ahmed.airesumebuilder.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
data class Skill(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val level: SkillLevel = SkillLevel.INTERMEDIATE,
    val category: String = "",

) : Parcelable

enum class SkillLevel {
    BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
}