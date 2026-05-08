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


) : Parcelable
