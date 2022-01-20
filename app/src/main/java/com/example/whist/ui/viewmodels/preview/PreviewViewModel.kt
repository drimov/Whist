package com.example.whist.ui.viewmodels.preview

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.whist.data.database.getDatabase
import com.example.whist.data.domain.Player
import com.example.whist.data.repository.PlayerRepository
import com.example.whist.data.repository.TableGameRepository
import com.example.whist.ui.view.PreviewFragmentArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class PreviewViewModel(app: Application, navArgs: PreviewFragmentArgs) : AndroidViewModel(app) {


    private val database = getDatabase(app)
    private val playerRepository = PlayerRepository(database)
//    private var _countPlayer = MutableLiveData<Int>()
//    val countPlayer : LiveData<Int>
//        get() = _countPlayer

    init {
        viewModelScope.launch {
            playerRepository.refreshPlayers(navArgs.table.id)
//            _countPlayer.value = playerRepository.countPlayer(navArgs.table.id).asLiveData().value
        }
    }

    val players = playerRepository.players(navArgs.table.id)
    val countPlayer = playerRepository.countPlayerInTable(navArgs.table.id)
    val table = navArgs.table


    /**
     * Factory Class
     */

    class Factory(val app: Application, private val navArgs: PreviewFragmentArgs) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PreviewViewModel::class.java)) {
                return PreviewViewModel(app, navArgs) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}