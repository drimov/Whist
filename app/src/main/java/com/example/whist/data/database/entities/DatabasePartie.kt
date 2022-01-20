package com.example.whist.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Player in database
 */
@Entity
data class DatabasePartie constructor(
    @PrimaryKey
    val id: Int
)