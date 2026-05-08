package com.ahmed.airesumebuilder.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
data class Experience(
    val id: String = UUID.randomUUID().toString(),
    val company: String = "",
    val jobTitle: String = "",
    val location: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val isCurrentJob: Boolean = false,
    val description: String = "",
    val achievement: List<String> = emptyList()
) : Parcelable
