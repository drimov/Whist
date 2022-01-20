package com.example.whist.data.network


import com.example.whist.data.database.entities.DatabaseTableGame
import com.example.whist.data.domain.TableGame
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * Container Network for player
 */
@JsonClass(generateAdapter = true)
data class NetworkTableContainer(val tables: List<NetworkTable>)
//data class NetworkTable(val tables: DatabaseTableGame)


/**
 * Player Network
 */
@JsonClass(generateAdapter = true)
data class NetworkTable(
    @Json(name ="IDTABLE") val id: Int,
    @Json(name ="NAMETABLE")val name: String,
    @Json(name ="ISPRIVATE")val isPrivate: Boolean,
    @Json(name ="ISCLOSE")val isClose: Boolean,
    @Json(name ="PWDTABLE") val password: String
)

/**
 * Convert Network list player in list player database
 */
fun NetworkTableContainer.asDatabaseModel(): Array<DatabaseTableGame> {
    return tables.map {
        DatabaseTableGame(
            id = it.id,
            name = it.name,
            isPrivate = it.isPrivate,
            isClose = it.isClose,
            password = it.password
        )
    }.toTypedArray()
}