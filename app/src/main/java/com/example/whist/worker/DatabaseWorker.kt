package com.example.whist.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.whist.data.database.getDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class CoroutineDatabaseWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("doWork Db", "try")

        return try {
            val database = getDatabase(applicationContext)

            withContext(Dispatchers.IO) {
                database.clearAllTables()
            }
            Log.d("doWork Db", "succes")
            Result.success()

        } catch (e: Exception) {
            Log.d("doWork DbException", "$e")
            Result.failure()
        }
    }
}