package com.example.whist.ui.viewmodels.room

import android.app.Application
import androidx.lifecycle.*
import com.example.whist.data.database.getDatabase
import com.example.whist.data.repository.FcmRepository
import com.example.whist.data.repository.TableGameRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RoomViewModel(app: Application) : AndroidViewModel(app) {

    private val database = getDatabase(app)
    private val roomRepository = TableGameRepository(database)
    private val fcmRepository = FcmRepository(database)

    init {
        viewModelScope.launch {
            roomRepository.refreshRoom()
        }
    }
    val room = roomRepository.tables


    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
                return RoomViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}