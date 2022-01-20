package com.example.whist.data.database.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.whist.data.database.entities.DatabasePlayer
import com.example.whist.data.domain.Player
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("select * from databaseplayer")
    fun getPlayers(): LiveData<List<DatabasePlayer>>

    @Query("select * from databaseplayer where tableId=:key")
    fun getPlayersFromTableId(key: Int): LiveData<List<DatabasePlayer>>

    @Query("select count(name) from databaseplayer where tableId=:key")
    fun getCountPlayerFromTableId(key: Int): Int

    @Query("select count(name) from databaseplayer where tableId=:key")
    fun getCountPlayerInTable(key: Int): LiveData<Int>

    @Query("delete from databaseplayer where name=:key")
    fun deletePlayer(key: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg players: DatabasePlayer)
}