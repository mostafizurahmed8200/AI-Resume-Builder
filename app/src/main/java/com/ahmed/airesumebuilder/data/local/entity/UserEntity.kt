package com.ahmed.airesumebuilder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ahmed.airesumebuilder.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val uid: String,
    val email: String,
    val displayName: String,
    val photoUrl: String,
    val createdAt: Long

) {
    fun toDomain(): User = User(
        uid = uid,
        email = email,
        displayName = displayName,
        photoUrl = photoUrl,
        createdAt = createdAt,
    )

    companion object {
        fun fromDomain(user: User): UserEntity =
            UserEntity(
                uid = user.uid,
                email = user.email,
                displayName = user.displayName,
                photoUrl = user.photoUrl,
                createdAt = user.createdAt,
            )

    }

}
