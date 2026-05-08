package com.ahmed.airesumebuilder.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
data class User(
    val id: String = UUID.randomUUID().toString(),
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable
