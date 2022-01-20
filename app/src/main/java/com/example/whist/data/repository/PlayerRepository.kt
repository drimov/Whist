package com.example.whist.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.whist.data.database.WhistDatabase
import com.example.whist.data.database.entities.asDomainModel
import com.example.whist.data.domain.Player
import com.example.whist.data.network.Network
import com.example.whist.data.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * Repository for fetching player from network and storing them on disk.
 */
class PlayerRepository(private val database: WhistDatabase) {


    /**
     * List Player that can be show on the screen.
     */
    fun players(tableId: Int): LiveData<List<Player>> =
        Transformations.map(database.playerDao.getPlayersFromTableId(tableId)) {
            it.asDomainModel()
        }

    fun countPlayerInTable(tableId: Int): LiveData<Int>{
        return database.playerDao.getCountPlayerInTable(tableId)
    }
    suspend fun countPlayer(tableId: Int): Int {
        return withContext(Dispatchers.IO){
            return@withContext database.playerDao.getCountPlayerFromTableId(tableId)
        }

    }

    suspend fun refreshPlayers(tableId: Int) {
        withContext(Dispatchers.IO) {
            val listPlayer = Network.whist.getPlayersAsync(tableId).await()
            database.playerDao.insertAll(*listPlayer.asDatabaseModel())
        }
    }

    suspend fun getPlayerName(name: String): Boolean {
        var result: Boolean

        coroutineScope {
            result = Network.whist.getPlayerExistAsync(name).await()
        }
        return result
    }

    suspend fun setPlayer(
        name: String,
        tableId: Int,
        isOwner: Boolean,
        tokenId: Int
    ): Response<String> {
        return withContext(Dispatchers.IO) {
            return@withContext Network.whist.setNewPlayerAsync(name, tableId, isOwner, tokenId)
        }
    }

    suspend fun deletePlayerRoom(name: String) {
        withContext(Dispatchers.IO) {
            database.playerDao.deletePlayer(name)
        }
    }

    suspend fun deletePlayer(name: String): Response<String> {
        return withContext(Dispatchers.IO) {
            Network.whist.deletePlayer(name)
        }
    }
}