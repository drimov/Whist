package com.example.whist.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.whist.data.database.entities.DatabaseFcm

@Dao
interface FcmDao {
    @Query("select * from databasefcm")
    fun getAllFcm(): LiveData<List<DatabaseFcm>>

    @Query("select * from databasefcm where id=:key")
    fun getFcmId(key: Int): LiveData<List<DatabaseFcm>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg fcm: DatabaseFcm)
}