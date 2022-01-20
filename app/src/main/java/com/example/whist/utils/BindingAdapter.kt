package com.example.whist.utils

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter
import com.example.whist.R
import com.google.android.material.textfield.TextInputLayout

/**
 * Binding adapter used to hide the spinner once data is available
 */
@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}

/**
 * Binding adapter used to choose the right icon
 */
@BindingAdapter("lockOrNot")
fun lockOrNot(view: ImageView, it: Boolean) {
    if (it) view.setImageResource(R.drawable.ic_round_lock_24)
    else view.setImageResource(R.drawable.ic_baseline_lock_open_24)
}

/**
 * Binding adapter used to hide or show password field
 */
@BindingAdapter("privateOrNot")
fun privateOrNot(view: View, it: Boolean) {
    view.visibility = if (it) View.VISIBLE else View.GONE
}

/**
 * Binding adapter used to hide or show button
 */
@BindingAdapter("count")
fun count(view: View, it: Int) {
    Log.d("BindingAdapeter","it: $it")
    view.visibility = if (it<7) View.VISIBLE else View.GONE
}

/**
 * Binding adapter used to write On or Off
 */
@BindingAdapter("togglePrivacy")
fun togglePrivacy(switch: SwitchCompat, isChecked: Boolean) {
    switch.text = if (isChecked) switch.textOn else switch.textOff
}
