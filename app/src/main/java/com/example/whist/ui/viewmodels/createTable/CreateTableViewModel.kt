package com.example.whist.ui.viewmodels.createTable

import android.app.Application
import android.util.Log
import androidx.collection.ArrayMap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.whist.data.database.getDatabase
import com.example.whist.data.domain.TableGame
import com.example.whist.data.repository.FcmRepository
import com.example.whist.data.repository.PlayerRepository
import com.example.whist.data.repository.TableGameRepository
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.lang.IllegalArgumentException

class CreateTableViewModel(app: Application) : AndroidViewModel(app) {

    private val database = getDatabase(app)
    private val playerRepository = PlayerRepository(database)
    private val tableGameRepository = TableGameRepository(database)
    private val fcmRepository = FcmRepository(database)

    private val _response = MutableLiveData<Response<String>>()
    val response: MutableLiveData<Response<String>>
        get() = _response


    private val _loadingTime = MutableLiveData<Boolean>()
    val loadingTime: MutableLiveData<Boolean>
        get() = _loadingTime

    private var _privacy = MutableLiveData<Boolean>()
    val privacy: MutableLiveData<Boolean>
        get() = _privacy

    init {
        //Default value
        if (_privacy.value == null) {
            _privacy.value = false
            _loadingTime.value = false
        }
    }

    fun stateLoading() {
        _loadingTime.value = !_loadingTime.value!!
    }

    fun changePrivacy() {
        _privacy.value = !_privacy.value!!
    }

    fun nameIsAvailable(name: String): Boolean {
        return runBlocking {
            playerRepository.getPlayerName(name)
        }
    }

    fun tableIsAvailable(name: String): Boolean {
        return runBlocking {
            tableGameRepository.getTableAvailable(name)
        }
    }

    fun getTableById(tableId: Int): TableGame {
        return runBlocking {
            tableGameRepository.getTableById(tableId)
        }
    }

    fun getTable(name: String): Int {
        return runBlocking {
            tableGameRepository.getTableByName(name)
        }
    }

    fun getFcm(token: String?): Int {
        return runBlocking {
            fcmRepository.getFcmByToken(token)
        }
    }

    fun insertTable(name: String, password: String) {
        runBlocking {
            _response.value = tableGameRepository.setTable(name, password)
        }
    }

    fun insertPlayer(name: String, tableId: Int, tokenId: Int): Response<String> {
        return runBlocking {
            return@runBlocking playerRepository.setPlayer(name, tableId, true, tokenId)
        }
    }

    fun sendUpdate(topicUpdate: String, data: Map<String, String>) {
        runBlocking {
            fcmRepository.sendMessage(topicUpdate, data)
        }
    }

    /**
     * Factory class
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CreateTableViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CreateTableViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}