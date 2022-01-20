package com.example.whist.data.database.entities

import androidx.lifecycle.Transformations.map
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.whist.data.domain.TableGame
import com.google.android.material.tabs.TabLayout


/**
 * TableGame in database
 */
@Entity
data class DatabaseTableGame constructor(
    @PrimaryKey
    val id: Int,
    val name: String,
    val isPrivate: Boolean,
    val isClose: Boolean,
    val password: String,
)

fun DatabaseTableGame.asDomainModel(): TableGame {
    return let {
        TableGame(
            id = it.id,
            name = it.name,
            isPrivate = it.isPrivate,
            isClose = it.isClose,
            password = it.password
        )
    }
}

/**
 * Convert TableGame in database in TableGame model
 */
fun List<DatabaseTableGame>.asDomainModel(): List<TableGame> {
    return map {
        TableGame(
            id = it.id,
            name = it.name,
            isPrivate = it.isPrivate,
            isClose = it.isClose,
            password = it.password
        )
    }
}