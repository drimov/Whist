package com.example.whist.ui.viewmodels.table

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.whist.data.database.getDatabase
import com.example.whist.data.domain.Player
import com.example.whist.data.domain.TableGame
import com.example.whist.data.repository.FcmRepository
import com.example.whist.data.repository.PlayerRepository
import com.example.whist.data.repository.TableGameRepository
import com.example.whist.ui.view.TableFragmentArgs
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response

class TableViewModel(app: Application, navArgs: TableFragmentArgs) : AndroidViewModel(app) {

    private val database = getDatabase(app)
    private val playerRepository = PlayerRepository(database)
    private val tableGameRepository = TableGameRepository(database)
    private val fcmRepository = FcmRepository(database)
    private val _response = MutableLiveData<Response<String>>()
    val response: LiveData<Response<String>>
        get() = _response
    private var _countPlayer = MediatorLiveData<Int>()
//    val countPlayer : LiveData<Int>
//    get() = _countPlayer


    init {
        viewModelScope.launch {
            playerRepository.refreshPlayers(navArgs.curentTable.id)
//            _countPlayer.value = playerRepository.countPlayer(navArgs.curentTable.id).asLiveData().value
        }
    }

    val players = playerRepository.players(navArgs.curentTable.id)
    val table = navArgs.curentTable.name


    fun getCountPlayer(tableId: Int): Int {
        return runBlocking {
            return@runBlocking playerRepository.countPlayer(tableId)
        }

    }


    fun deleteTable(tableId: Int) {
        runBlocking {
            tableGameRepository.deleteTable(tableId)
            tableGameRepository.deleteTableRoom(tableId)
        }
    }

    fun deletePlayer(namePlayer: String) {
        runBlocking {
            playerRepository.deletePlayer(namePlayer)
            playerRepository.deletePlayerRoom(namePlayer)
        }
    }

    fun sendUpdate(topicUpdate: String, data: Map<String, String>) {
        runBlocking {
            fcmRepository.sendMessage(topicUpdate, data)
        }
    }

    /**
     * Factory Class
     */

    class Factory(val app: Application, private val navArgs: TableFragmentArgs) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TableViewModel::class.java)) {
                return TableViewModel(app, navArgs) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}