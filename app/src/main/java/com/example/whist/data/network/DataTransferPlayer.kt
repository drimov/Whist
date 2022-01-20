package com.example.whist.data.network

import com.example.whist.data.database.entities.DatabasePlayer
import com.example.whist.data.domain.Player
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * Container Network for player
 */
@JsonClass(generateAdapter = true)
data class NetworkPlayerContainer(val players: List<NetworkPlayer>)


/**
 * Player Network
 */
@JsonClass(generateAdapter = true)
data class NetworkPlayer(
    @Json(name = "IDPLAYER") val id: Int,
    @Json(name = "USERNAMEPLAYER") val name: String,
    @Json(name = "IDTABLE") val tableId: Int,
    @Json(name = "ISOWNER") val isOwner: Boolean,
    @Json(name = "IDFCM") val fcmId: Int
)

/**
 * Convert Network results to database objects
 */
fun NetworkPlayerContainer.asDomainModel(): List<Player> {
    return players.map {
        Player(
            id = it.id,
            name = it.name,
            tableId = it.tableId,
            isOwner = it.isOwner,
            fcmId = it.fcmId
        )
    }
}

/**
 * Convert Network list player in list player database
 */
fun NetworkPlayerContainer.asDatabaseModel(): Array<DatabasePlayer> {
    return players.map {
        DatabasePlayer(
            id = it.id,
            name = it.name,
            tableId = it.tableId,
            isOwner = it.isOwner,
            fcmId = it.fcmId
        )
    }.toTypedArray()
}