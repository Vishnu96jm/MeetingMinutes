package com.xome.meetingminutes.utils

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.Toast
import com.xome.meetingminutes.R

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun createAndShowDialog(context: Context,
                        title: String,
                        message: String,
                        onPositiveAction: () -> Unit,
                        onNegativeAction: () -> Unit = {}) {
    val dialog = AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(R.string.yes) { dialog, _ ->
            onPositiveAction()
            dialog.dismiss()
        }
        .setNegativeButton(R.string.cancel) { dialog, _ ->
            onNegativeAction()
            dialog.dismiss()
        }
        .create()

    dialog.show()
}