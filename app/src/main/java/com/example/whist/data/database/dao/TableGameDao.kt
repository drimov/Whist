package com.example.whist.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.whist.data.database.entities.DatabaseTableGame

@Dao
interface TableGameDao {
    @Query("select * from databasetablegame")
    fun getTablesGame(): LiveData<List<DatabaseTableGame>>

    @Query("select * from databasetablegame where name=:key")
    fun getTablesGameByName(key: String): LiveData<DatabaseTableGame>

    @Query("select * from databasetablegame where id =:key")
    fun getTableGameById(key: Int): LiveData<DatabaseTableGame>

    @Query("delete from databasetablegame where id=:key")
    fun deleteTableGame(key: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg tablesGame: DatabaseTableGame)
}