package com.example.whist.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.whist.data.database.entities.DatabasePartie

@Dao
interface PartieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg partie: DatabasePartie)
}