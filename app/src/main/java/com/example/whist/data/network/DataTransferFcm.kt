package com.example.whist.data.network

import com.example.whist.data.database.entities.DatabaseFcm
import com.example.whist.data.domain.Fcm
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * Container Network for fcm
 */
@JsonClass(generateAdapter = true)
data class NetworkFcmContainer(val fcm: List<NetworkFcm>)


/**
 * Fcm Network
 */
@JsonClass(generateAdapter = true)
data class NetworkFcm(
    @Json(name = "IDFCM") val id: Int,
    @Json(name = "TOKEN") val token: String
)

/**
 * Convert Network results to database objects
 */
fun NetworkFcmContainer.asDomainModel(): List<Fcm> {
    return fcm.map {
        Fcm(
            id = it.id,
            token = it.token
        )
    }
}

/**
 * Convert Network list fcm in list fcm database
 */
fun NetworkFcmContainer.asDatabaseModel(): Array<DatabaseFcm> {
    return fcm.map {
        DatabaseFcm(
            id = it.id,
            token = it.token
        )
    }.toTypedArray()
}