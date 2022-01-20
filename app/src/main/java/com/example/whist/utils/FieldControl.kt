package com.example.whist.utils

import android.content.Context
import android.util.Log
import android.widget.EditText
import com.example.whist.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

private const val alphaNumeric: String = "^[\\w\\x20\\xC0-\\xFF]+"
private const val specialChar: String = "\\\"'><{=}|"

/**
 * listener on focus field,
 * return nothing or message if error
 * @option true = pwd / false = user
 */
fun focusListener(
    context: Context?,
    editText: EditText,
    textInputLayout: TextInputLayout,
    option: Boolean
) {
    editText.setOnFocusChangeListener { _, focused ->
        if (!focused) {
            textInputLayout.helperText =
                if (!option) validUserOrTable(context, editText) else validPassword(
                    context,
                    editText
                )
        }
    }
}

/**
 * @submitFrom valid or not field
 * @param arrayContainer Need to provide data in order : user, password
 * @param arrayEditText Same as arrayContainer
 */
fun submitFormLog(
    context: Context?,
    arrayContainer: ArrayList<TextInputLayout>,
    arrayEditText: ArrayList<EditText>,
    option: Boolean
): Boolean {

    arrayContainer[0].helperText = validUserOrTable(context, arrayEditText[0])
    if (option) {
        arrayContainer[1].helperText = validUserOrTable(context, arrayEditText[1])
    }

    val validUser = arrayContainer[0].helperText == null
    var validPwd: Boolean? = null
    var result = false

    if (option) {
        validPwd = arrayContainer[1].helperText == null
    }

    if (validUser) {
        if (option && validPwd == true || !option) {
            result = true
        }
    }
    return result
}

/**
 * @submitFrom valid or not field
 * @param arrayContainer Need to provide data in order : user,table, password
 * @param arrayEditText Same as arrayContainer
 */
fun submitFormCreate(
    context: Context?,
    arrayContainer: ArrayList<TextInputLayout>,
    arrayEditText: ArrayList<EditText>,
    option: Boolean
): Boolean {

    Log.d("submitForm", "ok")
    Log.d("size", "${arrayContainer.size}")


    val userText = arrayEditText[0]
    arrayContainer[0].helperText = validUserOrTable(context, userText)
    val tableText = arrayEditText[1]
    arrayContainer[1].helperText = validUserOrTable(context, tableText)

    val validUser = arrayContainer[0].helperText == null
    val validTable = arrayContainer[1].helperText == null
    var validPwd: Boolean? = null


    if (option) {
        val pwdText = arrayEditText[2]
        arrayContainer[2].helperText = validPassword(context, pwdText)
        validPwd = arrayContainer[2].helperText == null
    }
    var result = false

    if (validUser && validTable) {
        if (option && validPwd == true || !option) {
            result = true
        }
    }
    return result
}

fun resetFormCreate(
    context: Context?,
    arrayBoolean: ArrayList<Boolean>
) {
    val titleText = context?.getString(R.string.title_al)
    val textBtn = context?.getString(R.string.button_text_ad)
    var message = ""

    if (!arrayBoolean[0])
        message += context?.getString(R.string.msg_err_txt_ad_usr_exist)
    if (!arrayBoolean[1])
        message += context?.getString(R.string.msg_err_txt_ad_table_exist)

    createAlertDialog(titleText, message, textBtn, context).show()
}

fun resetFormLog(
    context: Context?,
    errorPwd: Boolean // If pwd wrong
) {
    val titleText = context?.getString(R.string.title_al)
    val textBtn = context?.getString(R.string.button_text_ad)
    var message = ""
    message += if (errorPwd) {
        context?.getString(R.string.msg_err_txt_ad_pwd_inc)
    } else {
        context?.getString(R.string.msg_err_txt_ad_usr_exist)
    }

    createAlertDialog(titleText, message, textBtn, context).show()
}


/**
 * @invalidFormJoin show error for form
 * @param arrayContainer contains player name, table and password if needed.
 * @param option true for password
 */
fun invalidFormCreate(
    context: Context?,
    arrayContainer: ArrayList<TextInputLayout>,
    option: Boolean
) {
    var message = ""
    val titleText = context?.getString(R.string.msg_err_txt_ad_if)
    val buttonText = context?.getString(R.string.button_text_ad)

    if (arrayContainer[0].helperText != null) {
        message += context?.getString(R.string.msg_err_txt_ad_usr) + arrayContainer[0].helperText
    }
    if (arrayContainer[1].helperText != null) {
        message += context?.getString(R.string.msg_err_txt_ad_table) + arrayContainer[1].helperText
    }
    if (option) {
        if (arrayContainer[2].helperText != null) {
            message += context?.getString(R.string.msg_err_txt_ad_pwd) + arrayContainer[2].helperText
        }
    }
    createAlertDialog(titleText, message, buttonText, context).show()
}

/**
 * @invalidFormJoin show error for form
 * @param arrayContainer contains player name and password if needed.
 * @param option true for password
 */
fun invalidFormLog(
    context: Context?,
    arrayContainer: ArrayList<TextInputLayout>,
    option: Boolean
) {
    var message = ""
    val titleText = context?.getString(R.string.msg_err_txt_ad_if)
    val buttonText = context?.getString(R.string.button_text_ad)
    if (arrayContainer[0].helperText != null)
        message += context?.getString(R.string.msg_err_txt_ad_usr) + arrayContainer[0].helperText
    if (option) {
        if (arrayContainer[1].helperText != null)
            message += context?.getString(R.string.msg_err_txt_ad_pwd) + arrayContainer[1].helperText
    }
    createAlertDialog(titleText, message, buttonText, context).show()
}

private fun createAlertDialog(
    title: String?,
    message: String,
    textBtn: String?,
    context: Context?
): MaterialAlertDialogBuilder {
    return MaterialAlertDialogBuilder(context!!)
        .setTitle(title)
        .setIcon(R.drawable.ic_baseline_warning_amber_24)
        .setMessage(message)
        .setPositiveButton(textBtn) { _, _ ->
        }
}

private fun validUserOrTable(context: Context?, editTextName: EditText): String? {
    val message = editTextName.text.toString()

    if (message.length < 4) {
        return context?.getString(R.string.msg_err_txt_nb)
    }
    if (message.matches(".*[\"'><{=}|].*".toRegex())) {
        return context?.getString(R.string.msg_err_txt_spc, specialChar)
    }
    if (!message.matches(alphaNumeric.toRegex())) {
        return context?.getString(R.string.msg_err_txt_cc)
    }
    return null
}

private fun validPassword(context: Context?, editTextPwd: EditText): String? {
    val pwdText = editTextPwd.text.toString()

    if (pwdText.length < 4) {
        return context?.getString(R.string.msg_err_txt_nb)
    }
//        if (!pwdText.matches(".*[a-z].*".toRegex())) {
//            return "Must contain 1 Lower-case character"
//        }
//        if (!pwdText.matches(".*[A-Z].*".toRegex())) {
//            return "Must contain 1 Upper-case character"
//        }
//        if(!pwdText.matches(".*[@\$#%^&+=].*".toRegex())){
//            return "Must contain 1 Special character @\$#%^&+="
//        }
    return null
}