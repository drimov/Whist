package com.example.whist.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.whist.data.database.getDatabase
import com.example.whist.data.repository.TableGameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class CoroutineRoomWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("doWork", "try")

        return try {
            val database = getDatabase(applicationContext)
            val tableGameRepository = TableGameRepository(database)
            val tableId = inputData.getString("tableId")

            withContext(Dispatchers.IO) {
                if (tableId != null) {
                    if (tableId.isNotEmpty()) tableGameRepository.deleteTableRoom(tableId.toInt())
                }
                tableGameRepository.refreshRoom()
            }
            Log.d("doWork", "succes")
            Result.success()

        } catch (e: Exception) {
            Log.d("doWork Exception", "$e")
            Result.failure()
        }
    }
}