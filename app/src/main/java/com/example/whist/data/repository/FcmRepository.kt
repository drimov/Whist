package com.example.whist.data.repository

import androidx.collection.ArrayMap
import com.example.whist.data.database.WhistDatabase
import com.example.whist.data.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class FcmRepository(private val database: WhistDatabase) {

//    val fcms: LiveData<List<Fcm>> = Transformations.map(database.fcmDao.getAllFcm()) {
//        it.asDomainModel()
//    }

    suspend fun setFcm(
            token: String?
    ): Response<Unit> {
        return withContext(Dispatchers.IO) {
            return@withContext Network.whist.setNewFcmAsync(token)
        }
    }

    suspend fun getFcmByToken(token: String?): Int {
        return withContext(Dispatchers.IO) {
            return@withContext Network.whist.getFcmAsync(token).await()
        }
    }

    suspend fun sendMessage(topic: String, data: Map<String,String>): Response<Unit> {
        return withContext(Dispatchers.IO) {
            return@withContext Network.whist.sendMessageAsync(topic,data)
        }
    }
}