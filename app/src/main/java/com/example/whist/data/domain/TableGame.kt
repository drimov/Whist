package com.example.whist.data.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * TableGame entity
 */
@Parcelize
data class TableGame(
    val id: Int,
    val name: String,
    val isPrivate: Boolean,
    val isClose: Boolean,
    val password: String
) : Parcelable