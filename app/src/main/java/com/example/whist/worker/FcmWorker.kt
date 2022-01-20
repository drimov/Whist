package com.example.whist.worker

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.whist.R
import com.example.whist.data.database.getDatabase
import com.example.whist.data.repository.FcmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception


class CoroutineFcmWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("doWork Fcm", "try")

        return try {
            val database = getDatabase(applicationContext)
            val fcmRepository = FcmRepository(database)
            val token = inputData.getString("token")

            val tokenName = applicationContext.getString(R.string.token_key)
            Log.d("token worker", token.toString())
//            saveTokenLocal(applicationContext, token)
            saveDataLocal(applicationContext, token, tokenName)
            withContext(Dispatchers.IO) {
                fcmRepository.setFcm(token.toString())
            }
            Log.d("doWork", "succes")
            Result.success()

        } catch (e: Exception) {
            Log.d("doWork Exception", "$e")
            Result.failure()
        }
    }
}

fun saveDataLocal(context: Context, data: String?, dataName: String) {
    val res = context.resources
    val sharedPrefs = context.getSharedPreferences(
        res.getString(R.string.preference_file_key), Context.MODE_PRIVATE
    ) ?: return
    with(sharedPrefs.edit()) {
        putString(dataName, data)
        apply()
    }
}

fun retrieveDataPref(context: Context, dataName: String): String? {
    val res = context.resources
    val sharedPrefs = context.getSharedPreferences(
        res.getString(R.string.preference_file_key), Context.MODE_PRIVATE
    )
    return sharedPrefs.getString(dataName, null)
}

fun deleteDataPref(context: Context, dataName: String) {
    val res = context.resources
    val sharedPrefs = context.getSharedPreferences(
        res.getString(R.string.preference_file_key), Context.MODE_PRIVATE
    ) ?: return
    with(sharedPrefs.edit()) {
        remove(dataName)
        commit()
    }
}