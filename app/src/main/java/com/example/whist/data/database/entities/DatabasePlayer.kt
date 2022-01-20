package com.example.whist.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.whist.data.domain.Player

/**
 * Player in database
 */
@Entity
data class DatabasePlayer constructor(
    @PrimaryKey
    val id: Int,
    val name: String,
    val tableId: Int,
    val isOwner: Boolean,
    val fcmId: Int,
)

/**
 * Convert player in database in player model
 */
fun List<DatabasePlayer>.asDomainModel(): List<Player> {
    return map {
        Player(
            id = it.id,
            name = it.name,
            tableId = it.tableId,
            isOwner = it.isOwner,
            fcmId = it.fcmId
        )
    }
}
