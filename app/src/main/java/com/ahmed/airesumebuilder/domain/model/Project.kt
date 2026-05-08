package com.ahmed.airesumebuilder.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
data class Project(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val description: String = "",
    val technologies: List<String> = emptyList(),
    val link: String = "",
    val startDate: String = "",
    val endDate: String = "",
) : Parcelable
