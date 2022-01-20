package com.example.whist.ui.viewmodels.login

import android.app.Application
import androidx.collection.ArrayMap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.whist.data.database.getDatabase
import com.example.whist.data.repository.FcmRepository
import com.example.whist.data.repository.PlayerRepository
import com.example.whist.ui.view.LoginTableFragmentArgs
import kotlinx.coroutines.runBlocking
import retrofit2.Response

class LoginTableViewModel(app: Application, navArgs: LoginTableFragmentArgs) :
    AndroidViewModel(app) {

    //    val table = tableGame
    private val _loadingTime = MutableLiveData<Boolean>()
    val loadingTime: MutableLiveData<Boolean>
        get() = _loadingTime

    private val _response = MutableLiveData<Response<String>>()
    val response: MutableLiveData<Response<String>>
        get() = _response


    private val database = getDatabase(app)
    private val playerRepository = PlayerRepository(database)
    private val fcmRepository = FcmRepository(database)
    val table = navArgs.table

    init {
        if (_loadingTime.value == null) {
            _loadingTime.value = false
        }

    }

    fun nameIsAvailable(name: String): Boolean {
        return runBlocking {
            playerRepository.getPlayerName(name)
        }
    }

    fun pwdIsCorrect(password: String): Boolean {
        return password == table.password
    }

    fun getFcm(token: String?): Int {
        return runBlocking {
            fcmRepository.getFcmByToken(token)
        }
    }

    fun insertPlayer(name: String, tableId: Int, tokenId: Int) {
        runBlocking {
            _response.value = playerRepository.setPlayer(name, tableId, false, tokenId)
        }
    }

    fun sendUpdate(topicUpdate: String, data: Map<String, String>) {
        runBlocking {
            fcmRepository.sendMessage(topicUpdate, data)
        }
    }

    fun stateLoading() {
        _loadingTime.value = !_loadingTime.value!!
    }

    /**
     * Factory class
     */
    class Factory(private val app: Application, private val navArgs: LoginTableFragmentArgs) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginTableViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginTableViewModel(app, navArgs) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}

