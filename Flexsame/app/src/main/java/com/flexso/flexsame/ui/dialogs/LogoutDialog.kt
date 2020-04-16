package com.flexso.flexsame.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.flexso.flexsame.MainActivity
import com.flexso.flexsame.R

class LogoutDialog(val myActivity: MainActivity) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return myActivity.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.dialog_logout)
                    .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { _, _ ->
                        myActivity.logout()
                    })
                    .setNegativeButton(R.string.no,
                            DialogInterface.OnClickListener { _, _ ->

                            })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")


    }
}