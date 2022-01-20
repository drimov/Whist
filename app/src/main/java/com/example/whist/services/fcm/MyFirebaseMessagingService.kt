package com.example.whist.services.fcm


import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.work.*
import com.example.whist.R
import com.example.whist.utils.togglePrivacy
import com.example.whist.worker.CoroutineFcmWorker
import com.example.whist.worker.CoroutineRoomWorker
import com.example.whist.worker.CoroutineTableWorker
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging
import kotlin.properties.Delegates

private const val TAG = "MyFirebaseMsgService"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        val entries = data.entries
        val room = data["room"]
        val preview = data["preview"]
        val table = data["table"]

        when {
            room != null -> {
                Log.d(TAG, " onMessageReceived - update room")
                var id: String? = null
                entries.forEach {
                    when (it.key) {
                        "room" -> id = it.value
                    }
                }
                Log.d(TAG, "deleteTable - $id")
                updateRoom(id)
            }
            preview != null -> {
                Log.d(TAG, "onMessageReceived - update preview")
            }
            table != null -> {
                Log.d(TAG, "onMessageReceived - table update")

                var id: Int? = null
                var namePlayer = ""

                entries.forEach {
                    when (it.key) {
                        "table" -> id = it.value.toInt()
                        "player" -> namePlayer = it.value
                    }
                }
                Log.d(TAG, "id - $id")
                Log.d(TAG, "namePlayer - $namePlayer")
                updateTable(id!!, namePlayer)
            }
        }
        super.onMessageReceived(remoteMessage)
    }

    private fun updateRoom(tableId: String?) {
        val data = workDataOf(
            "tableId" to tableId
        )
        val mRequest: WorkRequest = OneTimeWorkRequestBuilder<CoroutineRoomWorker>()
            .setInputData(data)
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueue(mRequest)
    }

    private fun updateTable(table: Int, namePlayer: String) {
        val data = workDataOf(
            "table" to table,
            "player" to namePlayer
        )
        val mRequest: WorkRequest = OneTimeWorkRequestBuilder<CoroutineTableWorker>()
            .setInputData(data)
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueue(mRequest)
    }


    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token)
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        Log.d(TAG, "sendRegistrationTokenToServer($token)")

        val data = workDataOf("token" to token)
        val mRequest: WorkRequest = OneTimeWorkRequestBuilder<CoroutineFcmWorker>()
            .setInputData(data)
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueue(mRequest)
    }
}

fun subscribeTopic(context: Context, topic: String) {
    val res = context.resources
    Firebase.messaging.subscribeToTopic(topic)
        .addOnCompleteListener { task ->
            var msg = res.getString(com.example.whist.R.string.msg_subscribed)
            if (!task.isSuccessful) {
                msg = res.getString(com.example.whist.R.string.msg_subscribe_failed)
            }
            Log.d(TAG, msg)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
}

fun unSubscribeTopic(context: Context, topic: String) {
    val res = context.resources
    Firebase.messaging.unsubscribeFromTopic(topic)
        .addOnCompleteListener { task ->
            var msg = res.getString(com.example.whist.R.string.msg_unsubscribed)
            if (!task.isSuccessful) {
                msg = res.getString(com.example.whist.R.string.msg_unsubscribe_failed)
            }
            Log.d(TAG, msg)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
}