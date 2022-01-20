package com.example.whist.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.whist.data.database.getDatabase
import com.example.whist.data.repository.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class CoroutineTableWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("TableWorker work", "try")

        return try {
            val database = getDatabase(applicationContext)
            val playerRepository = PlayerRepository(database)
            val tableId = inputData.getInt("table", 0)
            val namePlayer = inputData.getString("player")

            withContext(Dispatchers.IO) {
                playerRepository.deletePlayerRoom(namePlayer!!)
                playerRepository.refreshPlayers(tableId)
            }
            Log.d("TableWorker", "succes")
            Result.success()

        } catch (e: Exception) {
            Log.d("TableWorker Exception", "$e")
            Result.failure()
        }
    }
}