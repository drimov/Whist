package com.example.whist.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.whist.data.database.WhistDatabase
import com.example.whist.data.database.entities.asDomainModel
import com.example.whist.data.domain.TableGame
import com.example.whist.data.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response


/**
 * Repository for fetching player from network and storing them on disk.
 */
class TableGameRepository(private val database: WhistDatabase) {

    /**
     * Room that can be show on the screen.
     */

    val tables: LiveData<List<TableGame>> =
        Transformations.map(database.tableGameDao.getTablesGame()) {
            it.asDomainModel()
        }

    suspend fun refreshRoom() {
        withContext(Dispatchers.IO) {
            val room = Network.whist.getRoomAsync().await()
            database.tableGameDao.insertAll(*room.asDatabaseModel())
        }
    }


    suspend fun deleteTableRoom(tableId: Int){
        withContext(Dispatchers.IO) {
            database.tableGameDao.deleteTableGame(tableId)
        }
    }
    suspend fun deleteTable(tableId: Int): Response<String> {
        return withContext(Dispatchers.IO) {
            Network.whist.deleteTableGame(tableId)
        }
    }

    /**
     * @getTableById retrieve table
     * @param tableId table name given
     */
    suspend fun getTableById(tableId: Int): TableGame {
        return withContext(Dispatchers.IO) {
            return@withContext Network.whist.getTableByIdAsync(tableId).await()
                .asDatabaseModel()[0].asDomainModel()
        }
    }


    /**
     * @getTableName retrieve true or false with table name
     */
    suspend fun getTableAvailable(name: String): Boolean {
        return withContext(Dispatchers.IO) {
            return@withContext Network.whist.getTableExistAsync(name).await()
        }
    }

    /**
     * @getTableByName retrieve table ID
     * @param name table name given
     */
    suspend fun getTableByName(name: String): Int {
        return withContext(Dispatchers.IO) {
            return@withContext Network.whist.getTableAsync(name).await()
        }
    }


    suspend fun setTable(
        name: String,
        password: String
    ): Response<String> {

        return withContext(Dispatchers.IO) {
            return@withContext Network.whist.setNewTableAsync(name, password)
        }
    }
}