package com.ahmed.airesumebuilder.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
data class Education(
    val id: String = UUID.randomUUID().toString(),
    val institution: String = "",
    val degree: String = "",
    val fieldOfStudy: String = "",
    val graduationYear: String = "",
    val gpa: String = "",
    val achievement: String = "",
) : Parcelable
