package com.example.whist.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.whist.R
import com.example.whist.services.fcm.unSubscribeTopic
import com.example.whist.worker.deleteDataPref
import com.example.whist.worker.retrieveDataPref

class MyService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        Log.d("MyService", "onBind call")
        return null
    }

    override fun onDestroy() {
        Log.d("MyService", "onDestroy")
        val dataName = applicationContext.getString(R.string.topic)
        val topic = retrieveDataPref(applicationContext, dataName)
        if (topic != null) {
            Log.d("MyService", "topic not null")
            unSubscribeTopic(applicationContext, dataName)
            deleteDataPref(applicationContext, dataName)
        }
        super.onDestroy()
    }
}