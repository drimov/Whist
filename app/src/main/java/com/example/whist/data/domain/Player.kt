package com.example.whist.data.domain


/**
 * Player entity
 */
data class Player(
    val id: Int,
    val name: String,
    val tableId: Int,
    val isOwner: Boolean,
    val fcmId: Int
)