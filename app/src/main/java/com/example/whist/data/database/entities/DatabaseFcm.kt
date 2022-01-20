package com.example.whist.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.whist.data.domain.Fcm

@Entity
data class DatabaseFcm constructor(
    @PrimaryKey
    val id: Int,
    val token: String
)

/**
 * Convert player in database in player model
 */
fun List<DatabaseFcm>.asDomainModel(): List<Fcm> {
    return map {
        Fcm(
            id = it.id,
            token = it.token
        )
    }
}
