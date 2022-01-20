package com.example.whist


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.work.*
import com.example.whist.services.MyService
import com.example.whist.utils.clearDatabase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

const val TAG = "MAIN_ACTIVITY"

class MainActivity : AppCompatActivity() {
    companion object {

        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        getToken()
        deleteToken()
        clearData()
        setContentView(R.layout.activity_main)
        val intent = Intent(this, MyService::class.java)
        this.startService(intent)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy call")
        super.onDestroy()
    }
    //    private fun getDeviceId() {
//        val msg = Settings.Secure.getString(
//            applicationContext.contentResolver,
//            Settings.Secure.ANDROID_ID
//        )
//        Log.d("Device id:", msg)
//    }

    private fun clearData() {
        clearDatabase(applicationContext)
    }

    private fun deleteToken() {
        Firebase.messaging.deleteToken().addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(
                    com.example.whist.TAG,
                    "Delete FCM token failed",
                    task.exception
                )
                return@OnCompleteListener
            }
            getToken()
        })
    }

    private fun getToken() {
        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(
                    com.example.whist.TAG,
                    "Fetching FCM registration token failed",
                    task.exception
                )
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, msg)
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()

        })
    }
}