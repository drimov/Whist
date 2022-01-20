package com.example.whist.utils

import android.content.Context
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.whist.worker.CoroutineDatabaseWorker


fun clearDatabase(context: Context){
    val mRequest: WorkRequest = OneTimeWorkRequestBuilder<CoroutineDatabaseWorker>()
        .build()
    WorkManager.getInstance(context)
        .enqueue(mRequest)
}
fun navigationWithButton(button: Button, navigationDirection: NavDirections) {
    button.setOnClickListener {
        it.findNavController()
            .navigate(navigationDirection)
    }
}

fun navigationWithoutButton(fragment: Fragment, navigationDirection: NavDirections) {
    fragment.findNavController()
        .navigate(navigationDirection)
}

fun onbackPressedOff(fragmentActivity: FragmentActivity, lifecycleOwner: LifecycleOwner) {
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

        }
    }
    fragmentActivity.onBackPressedDispatcher.addCallback(lifecycleOwner, callback)
}

fun onbackPressedOn(
    fragmentActivity: FragmentActivity,
    lifecycleOwner: LifecycleOwner,
    fragment: Fragment,
    navigationDirection: NavDirections
) {
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navigationWithoutButton(fragment, navigationDirection)
        }
    }
    fragmentActivity.onBackPressedDispatcher.addCallback(lifecycleOwner, callback)
}